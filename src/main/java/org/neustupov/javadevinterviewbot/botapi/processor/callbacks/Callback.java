package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Callback {

  BotApiMethod<?> handleCallback(CallbackQuery callbackQuery, String callbackData,
      DataCache dataCache, int userId, Message message);
}
