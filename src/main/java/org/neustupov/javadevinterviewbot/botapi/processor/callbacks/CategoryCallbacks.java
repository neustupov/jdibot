package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.model.menu.Category;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Обрабатывает колбеки кнопок категорий
 */
@Component
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public class CategoryCallbacks implements Callback {

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

  public CategoryCallbacks(NavigationButtonCallbacks next,
      BotStateContext botStateContext, DataCache dataCache) {
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
      case OOP_CATEGORY_BUTTON:
        setShowCategoryBotState(userId);
        dataCache.setCategory(userId, Category.OOP);
        return botStateContext.processInputMessage(BotState.SHOW_CATEGORY, message);
      case COLLECTIONS_CATEGORY_BUTTON:
        setShowCategoryBotState(userId);
        dataCache.setCategory(userId, Category.COLLECTIONS);
        return botStateContext.processInputMessage(BotState.SHOW_CATEGORY, message);
      case PATTERNS_CATEGORY_BUTTON:
        setShowCategoryBotState(userId);
        dataCache.setCategory(userId, Category.PATTERNS);
        return botStateContext.processInputMessage(BotState.SHOW_CATEGORY, message);
      case SPRING_BUTTON:
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_SPRING_CATEGORY_MENU);
        dataCache.setCategory(userId, Category.SPRING);
        return botStateContext
            .processInputMessage(BotState.SHOW_SPRING_CATEGORY_MENU, message);
      case SPRING_PART_1_BUTTON:
      case SPRING_PART_2_BUTTON:
      case SPRING_PART_3_BUTTON:
        return sendAnswerCallbackQuery(NOT_WORK, true, callbackQuery);
    }

    return next.handleCallback(callbackQuery, userId, message);
  }

  /**
   * Устанавливает BotState.SHOW_CATEGORY в кеш пользователя
   *
   * @param userId userId
   */
  private void setShowCategoryBotState(int userId) {
    dataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY);
  }
}
