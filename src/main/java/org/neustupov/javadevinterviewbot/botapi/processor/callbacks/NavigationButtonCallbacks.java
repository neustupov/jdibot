package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import static org.neustupov.javadevinterviewbot.botapi.processor.callbacks.NavigationButtonCallbacks.Route.NEXT;
import static org.neustupov.javadevinterviewbot.botapi.processor.callbacks.NavigationButtonCallbacks.Route.PREVIOUS;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Обрабатывает колбеки кнопок навигации
 */
@Data
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NavigationButtonCallbacks implements Callback {

  /**
   * Контекст бота
   */
  BotStateContext botStateContext;

  /**
   * Кеш данных пользователя
   */
  DataCache dataCache;

  interface Route {

    String NEXT = "next";
    String PREVIOUS = "previous";
  }

  public NavigationButtonCallbacks(
      BotStateContext botStateContext, DataCache dataCache) {
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
      case BACK_BUTTON:
        dataCache.setUserCurrentBotState(userId, BotState.CATEGORY_OR_SEARCH_RESULT);
        dataCache.cleanRange(userId);
        return botStateContext.processInputMessage(BotState.CATEGORY_OR_SEARCH_RESULT, message);
      case BACK_TO_START_MENU_BUTTON:
        dataCache.cleanAll(userId);
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_START_MENU);
        return botStateContext.processInputMessage(BotState.SHOW_START_MENU, message);
      case BACK_TO_CATEGORY_BUTTON:
        dataCache.cleanCategory(userId);
        dataCache.cleanRange(userId);
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY_MENU);
        return botStateContext.processInputMessage(BotState.SHOW_CATEGORY_MENU, message);
      case BACK_TO_LEVEL_BUTTON:
        dataCache.cleanLevel(userId);
        dataCache.cleanCategory(userId);
        dataCache.cleanRange(userId);
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_LEVEL_MENU);
        return botStateContext.processInputMessage(BotState.SHOW_LEVEL_MENU, message);
      case NEXT_BUTTON:
        dataCache.setRoute(userId, NEXT);
        return botStateContext.processInputMessage(BotState.PAGINATION_PAGE, message);
      case PREVIOUS_BUTTON:
        dataCache.setRoute(userId, PREVIOUS);
        return botStateContext.processInputMessage(BotState.PAGINATION_PAGE, message);
    }
    return null;
  }
}
