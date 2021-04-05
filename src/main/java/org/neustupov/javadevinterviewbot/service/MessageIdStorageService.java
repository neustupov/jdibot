package org.neustupov.javadevinterviewbot.service;

import org.neustupov.javadevinterviewbot.model.MessageIdStorage;

public interface MessageIdStorageService {

  MessageIdStorage getStorageByChatId(long chatId);

  void save(MessageIdStorage messageIdStorage);
}
