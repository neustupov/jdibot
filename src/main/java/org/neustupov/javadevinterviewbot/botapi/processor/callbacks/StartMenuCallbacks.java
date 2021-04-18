package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Обрабатывает колбеки кнопок главного меню
 */
@Component
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public class StartMenuCallbacks implements Callback {

  /**
   * Следующий колбек
   */
  Callback next;

  /**
   * Контекст бота
   */
  BotStateContext botStateContext;

  /**
   * Кеш данных пользователя
   */
  DataCache dataCache;

  public StartMenuCallbacks(
      LevelCallbacks next, BotStateContext botStateContext,
      DataCache dataCache) {
    this.next = next;
    this.botStateContext = botStateContext;
    this.dataCache = dataCache;
  }

  /**
   * Обрабатывает колбек
   *
   * @param callbackQuery Колбек
   * @param userId userId
   * @param message Сообщение
   * @return Ответ приложения
   */
  @Override
  public BotApiMethod<?> handleCallback(CallbackQuery callbackQuery, int userId, Message message) {
    switch (getCallbackData(callbackQuery)) {
      case QUESTIONS_BUTTON:
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_LEVEL_MENU);
        return botStateContext.processInputMessage(BotState.SHOW_LEVEL_MENU, message);
      case SEARCH_BUTTON:
      case NEW_SEARCH_BUTTON:
        dataCache.cleanAll(userId);
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_SEARCH);
        return botStateContext.processInputMessage(BotState.SHOW_SEARCH, message);
      case TESTS_BUTTON:
        return sendAnswerCallbackQuery(NOT_WORK, true, callbackQuery);
    }

    return next.handleCallback(callbackQuery, userId, message);
  }
}
