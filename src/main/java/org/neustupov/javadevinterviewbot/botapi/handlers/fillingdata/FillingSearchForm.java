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
import org.neustupov.javadevinterviewbot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FillingSearchForm implements InputMessageHandler {

  ReplyMessageService replyMessageService;
  UserDataCache userDataCache;
  CategoryRepositoryInMemoryImpl categoryRepositoryInMemory;
  ButtonMaker buttonMaker;

  public FillingSearchForm(
      ReplyMessageService replyMessageService,
      UserDataCache userDataCache,
      CategoryRepositoryInMemoryImpl categoryRepositoryInMemory,
      ButtonMaker buttonMaker) {
    this.replyMessageService = replyMessageService;
    this.userDataCache = userDataCache;
    this.categoryRepositoryInMemory = categoryRepositoryInMemory;
    this.buttonMaker = buttonMaker;
  }

  @Override
  public SendMessage handle(Message message) {
    int userId = message.getFrom().getId();
    long chatId = message.getChatId();
    String searchStringFromUserCache = userDataCache.getUserContext((int) chatId).getSearchField();
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
      userDataCache.getUserContext(userId).setSearchField(searchString);
    }
    List<Question> qList = categoryRepositoryInMemory.search(searchString);
    SendMessage sendMessage = replyMessageService
        .getReplyMessage(chatId, "reply.empty-search-result");
    if (qList == null || qList.isEmpty()) {
      userDataCache.setUserCurrentBotState(userId, BotState.FILLING_SEARCH);
    } else {
      sendMessage.setText(parseQuestions(qList));
    }
    sendMessage.setReplyMarkup(buttonMaker.getBackFromSearchButtons());
    return sendMessage;
  }

  @Override
  public BotState getHandlerName() {
    return BotState.FILLING_SEARCH;
  }
}
