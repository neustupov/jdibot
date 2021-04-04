package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.model.buttons.ButtonCallbacks;
import org.neustupov.javadevinterviewbot.model.menu.Category;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCallbacks implements Callback {

  Callback next;
  BotStateContext botStateContext;

  public CategoryCallbacks(NavigationButtonCallbacks next,
      BotStateContext botStateContext) {
    this.next = next;
    this.botStateContext = botStateContext;
  }

  @Override
  public BotApiMethod<?> handleCallback(CallbackQuery callbackQuery, ButtonCallbacks callbackData,
      DataCache dataCache, int userId, Message message) {
    switch (callbackData) {
      case OOP_CATEGORY_BUTTON:
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY);
        dataCache.setCategory(userId, Category.OOP);
        return botStateContext.processInputMessage(BotState.SHOW_CATEGORY, message);
      case COLLECTIONS_CATEGORY_BUTTON:
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY);
        dataCache.setCategory(userId, Category.COLLECTIONS);
        return botStateContext.processInputMessage(BotState.SHOW_CATEGORY, message);
      case PATTERNS_CATEGORY_BUTTON:
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY);
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

    return next.handleCallback(callbackQuery, callbackData, dataCache, userId, message);
  }
}
