package org.neustupov.javadevinterviewbot.botapi.handlers.filldata;

import static org.neustupov.javadevinterviewbot.botapi.handlers.filldata.PaginationHandler.MenuPoint.*;
import static org.neustupov.javadevinterviewbot.botapi.handlers.filldata.PaginationHandler.Pagination.*;

import java.util.List;
import java.util.Optional;
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
 * Хендлер, обрабатывающий сообщения с пагинацией, в зависимости от предыдущего состояния заполняет
 * тело сообщения вопросами + добавляет кнопки <- и ->
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaginationHandler implements InputMessageHandler {

  /**
   * Направление пагинации
   */
  public interface Pagination {

    String PREVIOUS = "previous";
    String NEXT = "next";
  }

  /**
   * Текущее отображение меню
   */
  public interface MenuPoint {

    String CATEGORY = "category";
    String SEARCH = "search";
  }

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

  public PaginationHandler(DataCache dataCache,
      QuestionRepository questionRepository,
      ResponseMessageCreator responseMessageCreator) {
    this.dataCache = dataCache;
    this.questionRepository = questionRepository;
    this.responseMessageCreator = responseMessageCreator;
  }

  /**
   * Обрабатывает входящее сообщение, при необходимости добавляет кнопки пагинации
   *
   * @param message Входящее сообщение
   * @return SendMessage
   */
  @Override
  public SendMessage handle(Message message) {
    Long chatId = message.getChatId();
    UserContext userContext = dataCache.getUserContext(chatId.intValue());
    String searchStringFromUserCache = userContext.getSearchField();
    List<Question> qList = null;
    String categoryOrSearch = Optional.ofNullable(getCategoryOrSearch(chatId.intValue()))
        .orElse("");
    switch (categoryOrSearch) {
      case CATEGORY:
        qList = questionRepository
            .getAllByCategoryAndLevel(userContext.getCategory(), userContext.getLevel());
        break;
      case SEARCH:
        qList = questionRepository.search(searchStringFromUserCache);
        break;
    }
    String pagination = getPagination(chatId.intValue());
    return responseMessageCreator.getMessage(qList, chatId, chatId.intValue(), pagination);
  }

  /**
   * Проверяем необходимость пагинации
   *
   * @param userId userId
   * @return Направление пагинации
   */
  private String getPagination(int userId) {
    String route = dataCache.getUserContext(userId).getRoute();
    return route.equals(NEXT) || route.equals(PREVIOUS) ? route : null;
  }

  /**
   * Смотрим, из какого состояния мы сюда попали
   *
   * @param userId userId
   * @return Название состояния из которого мы пришли
   */
  private String getCategoryOrSearch(int userId) {
    UserContext userContext = dataCache.getUserContext(userId);
    return userContext.getCategory() != null ? CATEGORY
        : userContext.getSearchField() != null ? SEARCH : null;
  }

  /**
   * Возвращает сстояние, соответствующее хендлеру
   *
   * @return BotState
   */
  @Override
  public BotState getHandlerName() {
    return BotState.PAGINATION_PAGE;
  }
}
