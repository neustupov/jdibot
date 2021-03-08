package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallbackProcessor {

  DataCache dataCache;
  Callback callback;

  public CallbackProcessor(DataCache dataCache, StartMenuCallbacks callback) {
    this.dataCache = dataCache;
    this.callback = callback;
  }

  public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery)
      throws UnsupportedOperationException {
    final int userId = callbackQuery.getFrom().getId();
    final Message message = callbackQuery.getMessage();

    String callbackData = callbackQuery.getData();

    BotApiMethod<?> botResponse = callback
        .handleCallback(callbackQuery, callbackData, dataCache, userId, message);

    if (botResponse == null) {
      throw new UnsupportedOperationException("Callback handler not found");
    }

    return botResponse;
  }
}
