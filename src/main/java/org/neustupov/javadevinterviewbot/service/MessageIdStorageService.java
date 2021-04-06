package org.neustupov.javadevinterviewbot.service;

import org.neustupov.javadevinterviewbot.model.MessageIdStorage;

/**
 * Сервис объектов, хранящих данные о предыдущих сообщениях
 */
public interface MessageIdStorageService {

  /**
   * Возвращает хранилище по Id чата
   *
   * @param chatId chatId
   * @return MessageIdStorage
   */
  MessageIdStorage getStorageByChatId(long chatId);

  /**
   * Записывает хранилище
   *
   * @param messageIdStorage MessageIdStorage
   */
  void save(MessageIdStorage messageIdStorage);
}
