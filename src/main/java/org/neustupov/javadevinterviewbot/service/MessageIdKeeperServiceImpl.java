package org.neustupov.javadevinterviewbot.service;

import java.util.ArrayList;
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
  public void cleanImageId(long chatId) {
    Optional<MessageIdKeeper> messageIdKeeperOptional = messageIdKeeperRepository.findById(chatId);
    if (messageIdKeeperOptional.isPresent()) {
      MessageIdKeeper messageIdKeeper = messageIdKeeperOptional.get();
      messageIdKeeper.setImageMessageId(null);
      messageIdKeeperRepository.save(messageIdKeeper);
    }
  }

  /*@Override
  public void cleanMessageIdsList(long chatId) {
    Optional<MessageIdKeeper> messageIdKeeperOptional = messageIdKeeperRepository.findById(chatId);
    if (messageIdKeeperOptional.isPresent()) {
      MessageIdKeeper messageIdKeeper = messageIdKeeperOptional.get();
      messageIdKeeper.setPreviousMessageIdsList(new ArrayList<>());
      messageIdKeeperRepository.save(messageIdKeeper);
    }
  }*/
}
