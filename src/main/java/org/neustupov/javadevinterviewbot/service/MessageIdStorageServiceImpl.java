package org.neustupov.javadevinterviewbot.service;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.MessageIdStorage;
import org.neustupov.javadevinterviewbot.repository.MessageIdStorageRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageIdStorageServiceImpl implements MessageIdStorageService {

  private MessageIdStorageRepository messageIdStorageRepository;

  public MessageIdStorageServiceImpl(
      MessageIdStorageRepository messageIdStorageRepository) {
    this.messageIdStorageRepository = messageIdStorageRepository;
  }

  @Override
  public void save(MessageIdStorage messageIdStorage) {
    messageIdStorageRepository.save(messageIdStorage);
    log.info(
        "Save previousMessageId:{} needDeletePrevious:{} previousPreviousMessageId:{} needDeletePreviousPrevious:{} imageMessageId: {} needDeleteImage:{} for chatId:{}",
        messageIdStorage.getPreviousMessageId(),
        messageIdStorage.isNeedDeletePrevious(),
        messageIdStorage.getPreviousPreviousMessageId(),
        messageIdStorage.isNeedDeletePreviousPrevious(),
        messageIdStorage.getImageMessageId(),
        messageIdStorage.isNeedDeleteImage(),
        messageIdStorage.getChatId());
  }

  @Override
  public MessageIdStorage getStorageByChatId(long chatId) {
    Optional<MessageIdStorage> messageIdKeeperOptional = messageIdStorageRepository.findById(chatId);
    return messageIdKeeperOptional
        .orElseGet(() ->
            messageIdStorageRepository.save(GenericBuilder.of(MessageIdStorage::new)
                .with(MessageIdStorage::setChatId, chatId)
                .build()));
  }
}
