package org.neustupov.javadevinterviewbot.botapi.handlers.filldata;

import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.messagecreator.ResponseMessageCreator;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Хэндлер обрабатывающий состояние заполнения результатов поиска
 */
@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FillSearchHandler implements InputMessageHandler {

  /**
   * Кеш данных пользователя
   */
  final DataCache dataCache;

  /**
   * Репозиторий вопросов
   */
  final QuestionRepository questionRepository;

  /**
   * Класс, собирающий сообщение
   */
  final ResponseMessageCreator responseMessageCreator;

  @Value("${telegrambot.botUserName}")
  String botName;

  public FillSearchHandler(DataCache dataCache,
      QuestionRepository questionRepository,
      ResponseMessageCreator responseMessageCreator) {
    this.dataCache = dataCache;
    this.questionRepository = questionRepository;
    this.responseMessageCreator = responseMessageCreator;
  }

  /**
   * Обрабатывает запрос заполнения результатов поиска
   *
   * @param message Сообщение
   * @return SendMessage
   */
  @Override
  public SendMessage handle(Message message) {
    int userId = message.getFrom().getId();
    long chatId = message.getChatId();
    List<Question> qList = questionRepository.search(getSearchText(chatId, userId, message));
    return responseMessageCreator.getMessage(qList, chatId, userId, null);
  }

  /**
   * Ищет данные для поиска либо в кеше контекста пользователя, либо в самом входящем сообщении
   *
   * @param chatId chatId
   * @param userId userId
   * @param message Сообщение
   * @return Данные для поиска
   */
  private String getSearchText(long chatId, int userId, Message message) {
    String searchString = "";
    //Тут проблема в том, что при получении ответа по колбеку у нас сообщение приходит от бота а не от пользователя,
    //возможно проще и логичнее будет переделать на отдельные хендлера для колбеков
    String userName = message.getFrom().getUserName();
    if (userName == null || !userName.equals(botName.replace("@", ""))) {
      searchString = message.getText();
    }
    if (searchString == null || searchString.isEmpty()) {
      searchString = dataCache.getUserContext((int) chatId).getSearchField();
    } else {
      dataCache.setSearchField(userId, searchString);
    }
    return searchString;
  }

  /**
   * Возвращает сстояние, соответствующее хендлеру
   *
   * @return BotState
   */
  @Override
  public BotState getHandlerName() {
    return BotState.FILLING_SEARCH;
  }
}
