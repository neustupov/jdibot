package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.neustupov.javadevinterviewbot.utils.Emojis;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Callback {

  String NOT_WORK = Emojis.IN_DEV + " Раздел находится в разработке. " + Emojis.IN_DEV;

  BotApiMethod<?> handleCallback(CallbackQuery callbackQuery, String callbackData,
      DataCache dataCache, int userId, Message message);

  default AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert,
      CallbackQuery callbackQuery) {
    AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
    answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
    answerCallbackQuery.setShowAlert(alert);
    answerCallbackQuery.setText(text);
    return answerCallbackQuery;
  }
}
