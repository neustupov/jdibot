package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.*;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallbackProcessor {

  private static final String NOT_WORK = "В данный момент не поддерживается";

  UserDataCache userDataCache;
  BotStateContext botStateContext;

  public CallbackProcessor(UserDataCache userDataCache,
      BotStateContext botStateContext) {
    this.userDataCache = userDataCache;
    this.botStateContext = botStateContext;
  }

  public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery) {
    final int userId = callbackQuery.getFrom().getId();
    final Message message = callbackQuery.getMessage();
    BotApiMethod<?> callbackAnswer = null;

    String callbackData = callbackQuery.getData();

    switch (callbackData) {
      case QUESTIONS_BUTTON:
        userDataCache.setUserCurrentBotState(userId, BotState.SHOW_LEVEL_MENU);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_LEVEL_MENU, message);
        break;
      case SEARCH_BUTTON:
      case NEW_SEARCH_BUTTON:
        userDataCache.setUserCurrentBotState(userId, BotState.SHOW_SEARCH);
        userDataCache.cleanSearch(userId);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_SEARCH, message);
        break;
      case TESTS_BUTTON:
        callbackAnswer = sendAnswerCallbackQuery(NOT_WORK, true, callbackQuery);
        break;
      case JUNIOR_LEVEL_BUTTON:
        userDataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY_MENU);
        userDataCache.getUserContext(userId).setLevel(Level.JUNIOR);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_CATEGORY_MENU, message);
        break;
      case OOP_CATEGORY_BUTTON:
        userDataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY);
        userDataCache.getUserContext(userId).setCategory(Category.OOP);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_CATEGORY, message);
        break;
      case COLLECTIONS_CATEGORY_BUTTON:
        userDataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY);
        userDataCache.getUserContext(userId).setCategory(Category.COLLECTIONS);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_CATEGORY, message);
        break;
      case BACK_BUTTON:
        BotState previousBotState = userDataCache.getPreviousUserBotState(userId);
        callbackAnswer = botStateContext.processInputMessage(previousBotState, message);
        break;
      case BACK_TO_START_MENU_BUTTON:
        userDataCache.cleanStates(userId);
        userDataCache.cleanSearch(userId);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_START_MENU, message);
        break;
    }

    return callbackAnswer;
  }

  private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert,
      CallbackQuery callbackQuery) {
    AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
    answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
    answerCallbackQuery.setShowAlert(alert);
    answerCallbackQuery.setText(text);
    return answerCallbackQuery;
  }
}
