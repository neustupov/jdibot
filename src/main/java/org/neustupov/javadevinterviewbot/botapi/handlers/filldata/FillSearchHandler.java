package org.neustupov.javadevinterviewbot.botapi.handlers.filldata;

import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.messagecreator.ResponseMessageCreator;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.CommonQuestionRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FillSearchHandler implements InputMessageHandler {

  //TODO сообщения должен готовить ResponseMessageCreator вынести все туда
  DataCache dataCache;
  CommonQuestionRepository categoryRepositoryInMemory;
  ResponseMessageCreator responseMessageCreator;

  public FillSearchHandler(DataCache dataCache,
      CommonQuestionRepository categoryRepositoryInMemory,
      ResponseMessageCreator responseMessageCreator) {
    this.dataCache = dataCache;
    this.categoryRepositoryInMemory = categoryRepositoryInMemory;
    this.responseMessageCreator = responseMessageCreator;
  }

  @Override
  public SendMessage handle(Message message) {
    int userId = message.getFrom().getId();
    long chatId = message.getChatId();
    List<Question> qList = categoryRepositoryInMemory.search(getSearchText(chatId, userId, message));
    return responseMessageCreator.getMessage(qList, chatId, userId, null, null);
  }

  private String getSearchText(long chatId, int userId, Message message){
    String searchStringFromUserCache = dataCache.getUserContext((int) chatId).getSearchField();
    String searchString = "";
    //TODO тут проблема в том, что при получении ответа по колбеку у нас сообщение приходит от бота а не от пользователя,
    //возможно проще и логичнее будет переделать на отдельные хендлера для колбеков
    String userName = message.getFrom().getUserName();
    if (userName == null || !userName.equals("JavaDevInterBot")) {
      searchString = message.getText();
    }
    if (searchString == null || searchString.isEmpty()) {
      searchString = searchStringFromUserCache;
    } else {
      dataCache.getUserContext(userId).setSearchField(searchString);
    }
    return searchString;
  }

  @Override
  public BotState getHandlerName() {
    return BotState.FILLING_SEARCH;
  }
}