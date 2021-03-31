package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.*;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LevelCallbacks implements Callback {

  Callback next;
  BotStateContext botStateContext;

  public LevelCallbacks(CategoryCallbacks next,
      BotStateContext botStateContext) {
    this.next = next;
    this.botStateContext = botStateContext;
  }

  @Override
  public BotApiMethod<?> handleCallback(CallbackQuery callbackQuery, String callbackData,
      DataCache dataCache, int userId, Message message) {
    switch (callbackData) {
      case JUNIOR_LEVEL_BUTTON:
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY_MENU);
        dataCache.setUserLevel(userId, Level.JUNIOR);
        return botStateContext.processInputMessage(BotState.SHOW_CATEGORY_MENU, message);
      case MIDDLE_LEVEL_BUTTON:
      case SENIOR_LEVEL_BUTTON:
          return sendAnswerCallbackQuery(NOT_WORK, true, callbackQuery);
    }

    return next.handleCallback(callbackQuery, callbackData, dataCache, userId, message);
  }
}
