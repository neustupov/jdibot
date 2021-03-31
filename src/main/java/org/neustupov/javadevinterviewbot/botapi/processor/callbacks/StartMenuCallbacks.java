package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.NEW_SEARCH_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.QUESTIONS_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.SEARCH_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.TESTS_BUTTON;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StartMenuCallbacks implements Callback {

  Callback next;
  BotStateContext botStateContext;

  public StartMenuCallbacks(BotStateContext botStateContext,
      LevelCallbacks levelCallbacks) {
    this.botStateContext = botStateContext;
    this.next = levelCallbacks;
  }

  @Override
  public BotApiMethod<?> handleCallback(CallbackQuery callbackQuery, String callbackData,
      DataCache dataCache, int userId, Message message) {
    switch (callbackData) {
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

    return next.handleCallback(callbackQuery, callbackData, dataCache, userId, message);
  }
}
