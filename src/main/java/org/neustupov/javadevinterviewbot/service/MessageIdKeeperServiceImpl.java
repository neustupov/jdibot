package org.neustupov.javadevinterviewbot.service;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.MessageIdKeeper;
import org.neustupov.javadevinterviewbot.repository.MessageIdKeeperRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageIdKeeperServiceImpl implements MessageIdKeeperService {

  private MessageIdKeeperRepository messageIdKeeperRepository;

  public MessageIdKeeperServiceImpl(
      MessageIdKeeperRepository messageIdKeeperRepository) {
    this.messageIdKeeperRepository = messageIdKeeperRepository;
  }

  @Override
  public void save(MessageIdKeeper messageIdKeeper) {
    messageIdKeeperRepository.save(messageIdKeeper);
    log.info(
        "Save previousMessageId:{} needDeletePrevious:{} previousPreviousMessageId:{} needDeletePreviousPrevious:{} imageMessageId: {} needDeleteImage:{} for chatId:{}",
        messageIdKeeper.getPreviousMessageId(),
        messageIdKeeper.isNeedDeletePrevious(),
        messageIdKeeper.getPreviousPreviousMessageId(),
        messageIdKeeper.isNeedDeletePreviousPrevious(),
        messageIdKeeper.getImageMessageId(),
        messageIdKeeper.isNeedDeleteImage(),
        messageIdKeeper.getChatId());
  }

  @Override
  public MessageIdKeeper getKeeperByChatId(long chatId) {
    Optional<MessageIdKeeper> messageIdKeeperOptional = messageIdKeeperRepository.findById(chatId);
    return messageIdKeeperOptional
        .orElseGet(() ->
            messageIdKeeperRepository.save(GenericBuilder.of(MessageIdKeeper::new)
                .with(MessageIdKeeper::setChatId, chatId)
                .build()));
  }

  @Override
  public MessageIdKeeper updateMessageIdKeeper(long chatId, Boolean needDeletePrevious,
      Integer previousMessageId, Boolean needDeletePreviousPrevious,
      Integer previousPreviousMessageId, Boolean needDeleteImage, Integer imageMessageId) {
    Optional<MessageIdKeeper> messageIdKeeperOptional = messageIdKeeperRepository.findById(chatId);
    MessageIdKeeper messageIdKeeper = null;
    if (messageIdKeeperOptional.isPresent()) {
      messageIdKeeper = messageIdKeeperOptional.get();
      if (needDeletePrevious != null) {
        messageIdKeeper.setNeedDeletePrevious(needDeletePrevious);
      }
      if (previousMessageId != null) {
        messageIdKeeper.setPreviousMessageId(previousMessageId);
      }
      if (needDeletePreviousPrevious != null) {
        messageIdKeeper.setNeedDeletePreviousPrevious(needDeletePreviousPrevious);
      }
      if (previousPreviousMessageId != null) {
        messageIdKeeper.setPreviousPreviousMessageId(previousPreviousMessageId);
      }
      if (needDeleteImage != null) {
        messageIdKeeper.setNeedDeleteImage(needDeleteImage);
      }
      if (imageMessageId != null) {
        messageIdKeeper.setImageMessageId(imageMessageId);
      }
      save(messageIdKeeper);
    }
    return messageIdKeeper;
  }
}
