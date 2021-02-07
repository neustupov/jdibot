package org.neustupov.javadevinterviewbot.botapi.handlers.fillingdata;

import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.CategoryRepositoryInMemoryImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FillingSearchForm implements InputMessageHandler {

  static final String EMPTY_RESULT_MESSAGE = "Результаты поиска отсутствуют, введите фразу ещё раз";

  UserDataCache userDataCache;
  CategoryRepositoryInMemoryImpl categoryRepositoryInMemory;
  ButtonMaker buttonMaker;

  public FillingSearchForm(UserDataCache userDataCache,
      CategoryRepositoryInMemoryImpl categoryRepositoryInMemory,
      ButtonMaker buttonMaker) {
    this.userDataCache = userDataCache;
    this.categoryRepositoryInMemory = categoryRepositoryInMemory;
    this.buttonMaker = buttonMaker;
  }

  @Override
  public SendMessage handle(Message message) {
    int userId = message.getFrom().getId();
    long chatId = message.getChatId();
    userDataCache.setUserCurrentBotState(userId, BotState.SHOW_SEARCH_RESULT);
    String searchStringFromUserCache = userDataCache.getUserContext(userId).getSearchField();
    String searchString = message.getText();
    if (searchString == null || searchString.isEmpty()){
      searchString = searchStringFromUserCache;
    } else {
      userDataCache.getUserContext(userId).setSearchField(searchString);
    }
    List<Question> qList = categoryRepositoryInMemory.search(searchString);
    SendMessage sendMessage = new SendMessage(chatId, "");
    if (qList == null || qList.isEmpty()){
      sendMessage.setText(EMPTY_RESULT_MESSAGE);
      userDataCache.setUserCurrentBotState(userId, BotState.FILLING_SEARCH);
    } else {
      sendMessage.setText(parseQuestions(qList));
      sendMessage.setReplyMarkup(buttonMaker.getBackFromSearchButtons());
    }
    return sendMessage;
  }

  @Override
  public BotState getHandlerName() {
    return BotState.FILLING_SEARCH;
  }
}
