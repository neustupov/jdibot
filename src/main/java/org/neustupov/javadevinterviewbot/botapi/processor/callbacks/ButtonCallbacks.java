package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.BACK_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.BACK_TO_CATEGORY_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.BACK_TO_LEVEL_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.BACK_TO_START_MENU_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.NEXT_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.PREVIOUS_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.processor.callbacks.ButtonCallbacks.Route.NEXT;
import static org.neustupov.javadevinterviewbot.botapi.processor.callbacks.ButtonCallbacks.Route.PREVIOUS;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Data
@Component
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ButtonCallbacks implements Callback{

  BotStateContext botStateContext;

  interface Route {
    String NEXT = "next";
    String PREVIOUS = "previous";
  }

  public ButtonCallbacks(BotStateContext botStateContext) {
    this.botStateContext = botStateContext;
  }

  @Override
  public BotApiMethod<?> handleCallback(CallbackQuery callbackQuery, String callbackData,
      DataCache dataCache, int userId, Message message) {
    switch (callbackData) {
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
