package org.neustupov.javadevinterviewbot.service;

import org.neustupov.javadevinterviewbot.model.MessageIdKeeper;

public interface MessageIdKeeperService {

  MessageIdKeeper getKeeperByChatId(long chatId);
  void save(MessageIdKeeper messageIdKeeper);
}
