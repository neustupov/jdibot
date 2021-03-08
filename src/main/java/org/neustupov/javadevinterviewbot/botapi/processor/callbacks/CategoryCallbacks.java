package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.COLLECTIONS_CATEGORY_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.OOP_CATEGORY_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.PATTERNS_CATEGORY_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.SPRING_BUTTON;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
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

  public CategoryCallbacks(ButtonCallbacks next,
      BotStateContext botStateContext) {
    this.next = next;
    this.botStateContext = botStateContext;
  }

  @Override
  public BotApiMethod<?> handleCallback(CallbackQuery callbackQuery, String callbackData,
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
    }

    return next.handleCallback(callbackQuery, callbackData, dataCache, userId, message);
  }
}
