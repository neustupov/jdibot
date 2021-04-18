package org.neustupov.javadevinterviewbot.service;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.MessageIdStorage;
import org.neustupov.javadevinterviewbot.repository.MessageIdStorageRepository;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса хранилищ предыдущих сообщений
 */
@Slf4j
@Service
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public class MessageIdStorageServiceImpl implements MessageIdStorageService {

  /**
   * Репозиторий хранилищь предыдущих сообщений
   */
  MessageIdStorageRepository messageIdStorageRepository;

  public MessageIdStorageServiceImpl(
      MessageIdStorageRepository messageIdStorageRepository) {
    this.messageIdStorageRepository = messageIdStorageRepository;
  }

  /**
   * Записывает хранилище
   *
   * @param messageIdStorage MessageIdStorage
   */
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

  /**
   * Возвращает хранилище по Id чата
   *
   * @param chatId chatId
   * @return MessageIdStorage
   */
  @Override
  public MessageIdStorage getStorageByChatId(long chatId) {
    Optional<MessageIdStorage> messageIdKeeperOptional = messageIdStorageRepository
        .findById(chatId);
    return messageIdKeeperOptional
        .orElseGet(() ->
            messageIdStorageRepository.save(GenericBuilder.of(MessageIdStorage::new)
                .with(MessageIdStorage::setChatId, chatId)
                .build()));
  }
}
