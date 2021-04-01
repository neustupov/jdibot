package org.neustupov.javadevinterviewbot.service;

import org.neustupov.javadevinterviewbot.model.MessageIdKeeper;

public interface MessageIdKeeperService {

  MessageIdKeeper getKeeperByChatId(long chatId);

  void save(MessageIdKeeper messageIdKeeper);

  MessageIdKeeper updateMessageIdKeeper(long chatId, Boolean needDeletePrevious, Integer previousMessageId,
      Boolean needDeletePreviousPrevious, Integer previousPreviousMessageId,
      Boolean needDeleteImage, Integer imageMessageId);
}
