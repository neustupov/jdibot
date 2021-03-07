package org.neustupov.javadevinterviewbot.botapi.handlers.common;

import static org.neustupov.javadevinterviewbot.botapi.states.BotState.CATEGORY_OR_SEARCH_RESULT;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.handlers.filldata.FillSearchHandler;
import org.neustupov.javadevinterviewbot.botapi.handlers.lists.CategoryListsHandler;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Хендлер выбора действия для кнопки возврата
 */
@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryOrSearchResultHandler implements InputMessageHandler {

  CategoryListsHandler categoryListsHandler;
  FillSearchHandler fillSearchHandler;
  UserDataCache userDataCache;

  public CategoryOrSearchResultHandler(
      CategoryListsHandler categoryListsHandler,
      FillSearchHandler fillSearchHandler,
      UserDataCache userDataCache) {
    this.categoryListsHandler = categoryListsHandler;
    this.fillSearchHandler = fillSearchHandler;
    this.userDataCache = userDataCache;
  }

  @Override
  public SendMessage handle(Message message) {
    int userId = message.getChatId().intValue();
    UserContext userContext = userDataCache.getUserContext(userId);
    String userSearchField = userContext.getSearchField();
    Category userCategory = userContext.getCategory();
    if (userSearchField != null && !userSearchField.isEmpty()) {
      userDataCache.setUserCurrentBotState(userId, BotState.FILLING_SEARCH);
      return fillSearchHandler.handle(message);
    } else if (userCategory != null) {
      userDataCache.setUserCurrentBotState(userId, BotState.SHOW_CATEGORY);
      return categoryListsHandler.handle(message);
    }
    return null;
  }

  @Override
  public BotState getHandlerName() {
    return CATEGORY_OR_SEARCH_RESULT;
  }
}
