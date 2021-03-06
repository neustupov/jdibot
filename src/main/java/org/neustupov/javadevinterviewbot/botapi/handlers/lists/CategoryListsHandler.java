package org.neustupov.javadevinterviewbot.botapi.handlers.lists;

import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.messagecreator.ResponseMessageCreator;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.neustupov.javadevinterviewbot.repository.QuestionRepositoryMongo;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryListsHandler implements InputMessageHandler {

  //TODO сообщения должен готовить ResponseMessageCreator вынести все туда
  DataCache dataCache;
  QuestionRepositoryMongo questionRepository;
  ResponseMessageCreator responseMessageCreator;

  public CategoryListsHandler(DataCache dataCache,
      QuestionRepositoryMongo questionRepository,
      ResponseMessageCreator responseMessageCreator) {
    this.dataCache = dataCache;
    this.questionRepository = questionRepository;
    this.responseMessageCreator = responseMessageCreator;
  }

  @Override
  public SendMessage handle(Message message) {
    Long chatId = message.getChatId();
    UserContext userContext = dataCache.getUserContext(chatId.intValue());
    List<Question> qList = questionRepository
        .getAllByCategoryAndLevel(userContext.getCategory(), userContext.getLevel());
    return responseMessageCreator.getMessage(qList, chatId, chatId.intValue(), null);
  }

  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_CATEGORY;
  }
}
