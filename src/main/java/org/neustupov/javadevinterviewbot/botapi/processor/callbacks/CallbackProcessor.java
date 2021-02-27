package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.*;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.cache.DataCache;
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

  DataCache dataCache;
  BotStateContext botStateContext;

  public CallbackProcessor(DataCache dataCache,
      BotStateContext botStateContext) {
    this.dataCache = dataCache;
    this.botStateContext = botStateContext;
  }

  //TODO для категорий будет много кнопок - лучше вынести обработку всех кнопок в отдельные бины и
  //сделать цепочку обязанностей
  public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery) {
    final int userId = callbackQuery.getFrom().getId();
    final Message message = callbackQuery.getMessage();
    BotApiMethod<?> callbackAnswer = null;

    String callbackData = callbackQuery.getData();

    switch (callbackData) {
      case QUESTIONS_BUTTON:
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_LEVEL_MENU);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_LEVEL_MENU, message);
        break;
      case SEARCH_BUTTON:
      case NEW_SEARCH_BUTTON:
        dataCache.cleanAll(userId);
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_SEARCH);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_SEARCH, message);
        break;
      case TESTS_BUTTON:
        callbackAnswer = sendAnswerCallbackQuery(NOT_WORK, true, callbackQuery);
        break;
      case JUNIOR_LEVEL_BUTTON:
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY_MENU);
        dataCache.getUserContext(userId).setLevel(Level.JUNIOR);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_CATEGORY_MENU, message);
        break;
      case OOP_CATEGORY_BUTTON:
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY);
        dataCache.getUserContext(userId).setCategory(Category.OOP);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_CATEGORY, message);
        break;
      case COLLECTIONS_CATEGORY_BUTTON:
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY);
        dataCache.getUserContext(userId).setCategory(Category.COLLECTIONS);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_CATEGORY, message);
        break;
      case SPRING_BUTTON:
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_SPRING_CATEGORY_MENU);
        dataCache.getUserContext(userId).setCategory(Category.SPRING);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_SPRING_CATEGORY_MENU, message);
        break;
      case BACK_BUTTON:
        dataCache.setUserCurrentBotState(userId, BotState.CATEGORY_OR_SEARCH_RESULT);
        dataCache.cleanRange(userId);
        callbackAnswer = botStateContext.processInputMessage(BotState.CATEGORY_OR_SEARCH_RESULT, message);
        break;
      case BACK_TO_START_MENU_BUTTON:
        dataCache.cleanAll(userId);
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_START_MENU);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_START_MENU, message);
        break;
      case BACK_TO_CATEGORY_BUTTON:
        dataCache.cleanCategory(userId);
        dataCache.cleanRange(userId);
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY_MENU);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_CATEGORY_MENU, message);
        break;
      case BACK_TO_LEVEL_BUTTON:
        dataCache.cleanLevel(userId);
        dataCache.cleanCategory(userId);
        dataCache.cleanRange(userId);
        dataCache.setUserCurrentBotState(userId, BotState.SHOW_LEVEL_MENU);
        callbackAnswer = botStateContext.processInputMessage(BotState.SHOW_LEVEL_MENU, message);
        break;
      case NEXT_BUTTON:
        dataCache.getUserContext(userId).setRoute("next");
        callbackAnswer = botStateContext.processInputMessage(BotState.PAGINATION_PAGE, message);
        break;
      case PREVIOUS_BUTTON:
        dataCache.getUserContext(userId).setRoute("previous");
        callbackAnswer = botStateContext.processInputMessage(BotState.PAGINATION_PAGE, message);
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
