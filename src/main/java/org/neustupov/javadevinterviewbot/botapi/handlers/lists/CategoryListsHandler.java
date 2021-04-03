package org.neustupov.javadevinterviewbot.botapi.handlers.lists;

import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.messagecreator.ResponseMessageCreator;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.neustupov.javadevinterviewbot.repository.QuestionRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Хэндлер списка категорий
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryListsHandler implements InputMessageHandler {

  /**
   * Кеш данных пользователя
   */
  DataCache dataCache;

  /**
   * Репозиторий вопросов
   */
  QuestionRepository questionRepository;

  /**
   * Класс, собирающий сообщение
   */
  ResponseMessageCreator responseMessageCreator;

  public CategoryListsHandler(DataCache dataCache,
      QuestionRepository questionRepository,
      ResponseMessageCreator responseMessageCreator) {
    this.dataCache = dataCache;
    this.questionRepository = questionRepository;
    this.responseMessageCreator = responseMessageCreator;
  }

  /**
   * Обрабатывает входящее сообщение
   *
   * @param message Входящее сообщение
   * @return SendMessage
   */
  @Override
  public SendMessage handle(Message message) {
    Long chatId = message.getChatId();
    UserContext userContext = dataCache.getUserContext(chatId.intValue());
    List<Question> qList = questionRepository
        .getAllByCategoryAndLevel(userContext.getCategory(), userContext.getLevel());
    return responseMessageCreator.getMessage(qList, chatId, chatId.intValue(), null);
  }

  /**
   * Возвращает сстояние, соответствующее хендлеру
   *
   * @return BotState
   */
  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_CATEGORY;
  }
}
