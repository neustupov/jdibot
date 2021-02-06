package org.neustupov.javadevinterviewbot.botapi.handlers.fillingdata;

import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

  UserDataCache userDataCache;
  CategoryRepositoryInMemoryImpl categoryRepositoryInMemory;

  public FillingSearchForm(UserDataCache userDataCache,
      CategoryRepositoryInMemoryImpl categoryRepositoryInMemory) {
    this.userDataCache = userDataCache;
    this.categoryRepositoryInMemory = categoryRepositoryInMemory;
  }

  @Override
  public SendMessage handle(Message message) {
    int userId = message.getFrom().getId();
    long chatId = message.getChatId();
    userDataCache.setUserCurrentBotState(userId, BotState.SHOW_SEARCH_RESULT);

    String searchString = message.getText();
    List<Question> qList = categoryRepositoryInMemory.search(searchString);
    SendMessage sendMessage = new SendMessage(chatId, "");
    if (qList == null || qList.isEmpty()){
      sendMessage.setText("Результаты поиска отсутствуют, введите фразу ещё раз");
      userDataCache.setUserCurrentBotState(userId, BotState.FILLING_SEARCH);
    } else {
      sendMessage.setText(parseQuestions(qList));
    }
    return sendMessage;
  }

  @Override
  public BotState getHandlerName() {
    return BotState.FILLING_SEARCH;
  }
}
