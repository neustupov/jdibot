package org.neustupov.javadevinterviewbot.botapi.handlers.filldata;

import static org.neustupov.javadevinterviewbot.botapi.handlers.filldata.PaginationHandler.Pagination.NEXT;
import static org.neustupov.javadevinterviewbot.botapi.states.BotState.PAGINATION_PAGE;

import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.messagecreator.ResponseMessageCreator;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.CommonQuestionRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaginationHandler implements InputMessageHandler {

  public interface Pagination {

    String PREVIOUS = "previous";
    String NEXT = "next";
  }

  DataCache dataCache;
  CommonQuestionRepository commonQuestionRepository;
  ResponseMessageCreator responseMessageCreator;

  public PaginationHandler(DataCache dataCache,
      CommonQuestionRepository commonQuestionRepository,
      ResponseMessageCreator responseMessageCreator) {
    this.dataCache = dataCache;
    this.commonQuestionRepository = commonQuestionRepository;
    this.responseMessageCreator = responseMessageCreator;
  }

  @Override
  public SendMessage handle(Message message) {
    Long chatId = message.getChatId();
    String searchStringFromUserCache = dataCache.getUserContext(chatId.intValue()).getSearchField();
    List<Question> qList = null;
    String categoryOrSearch = getCategoryOrSearch(chatId.intValue());
    if (Objects.equals(categoryOrSearch, "category")) {
      qList = commonQuestionRepository
          .getAllByCategoryAndLevel(dataCache.getUserContext(chatId.intValue()).getCategory(),
              dataCache.getUserContext(chatId.intValue()).getLevel());
    } else if (Objects.equals(categoryOrSearch, "search")) {
      qList = commonQuestionRepository.search(searchStringFromUserCache);
    }
    String pagination = getPagination(chatId.intValue());
    return responseMessageCreator.getMessage(qList, chatId, chatId.intValue(), pagination);
  }

  @Override
  public BotState getHandlerName() {
    return PAGINATION_PAGE;
  }

  private String getPagination(int userId) {
    String route = dataCache.getUserContext(userId).getRoute();
    if (route.equals("next")) {
      return NEXT;
    } else if (route.equals("previous")) {
      return "previous";
    }
    return null;
  }

  private String getCategoryOrSearch(int userId) {
    if (dataCache.getUserContext(userId).getCategory() != null) {
      return "category";
    } else if (dataCache.getUserContext(userId).getSearchField() != null) {
      return "search";
    }
    return null;
  }
}
