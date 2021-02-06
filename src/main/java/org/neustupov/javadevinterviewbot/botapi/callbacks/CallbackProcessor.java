package org.neustupov.javadevinterviewbot.botapi.callbacks;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallbackProcessor {

  UserDataCache userDataCache;
  BotStateContext botStateContext;

  public CallbackProcessor(UserDataCache userDataCache,
      BotStateContext botStateContext) {
    this.userDataCache = userDataCache;
    this.botStateContext = botStateContext;
  }

  public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery){
    final int userId = callbackQuery.getFrom().getId();
    final Message message = callbackQuery.getMessage();
    BotApiMethod<?> callbackAnswer = null;

    String calbackData = callbackQuery.getData();

    switch (calbackData) {
      case "buttonQuestions":
        userDataCache.setUserCurrentBotState(userId, BotState.SHOW_LEVEL_MENU);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_LEVEL_MENU, message);
        break;
      case "buttonJunior":
        userDataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY_MENU);
        userDataCache.getUserContext(userId).setLevel(Level.JUNIOR);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_CATEGORY_MENU, message);
        break;
      case "buttonOOP":
        userDataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY);
        userDataCache.getUserContext(userId).setCategory(Category.OOP);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_CATEGORY, message);
        break;
      case "backButton":
        BotState previousBotState = userDataCache.getPreviousUserBotState(userId);
        userDataCache.setUserCurrentBotState(userId, previousBotState);
        callbackAnswer = botStateContext.processInputMessage(previousBotState, message);
        break;
    }

    return callbackAnswer;
  }
}
