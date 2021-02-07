package org.neustupov.javadevinterviewbot.botapi.handlers.lists;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.neustupov.javadevinterviewbot.repository.CategoryRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryListsHandler implements InputMessageHandler {

  UserDataCache userDataCache;
  CategoryRepository categoryRepository;
  ButtonMaker buttonMaker;

  public CategoryListsHandler(UserDataCache userDataCache,
      CategoryRepository categoryRepository,
      ButtonMaker buttonMaker) {
    this.userDataCache = userDataCache;
    this.categoryRepository = categoryRepository;
    this.buttonMaker = buttonMaker;
  }

  @Override
  public SendMessage handle(Message message) {
    long chatId = message.getChatId();
    UserContext userContext = userDataCache.getUserContext((int)chatId);
    List<Question> qList =  categoryRepository.getAllQuestionsByCategoryAndLevel(userContext.getCategory(), userContext.getLevel());
    SendMessage sm = new SendMessage(chatId, parseQuestions(qList));
    sm.setReplyMarkup(buttonMaker.getBackButton());
    return sm;
  }

  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_CATEGORY;
  }
}
