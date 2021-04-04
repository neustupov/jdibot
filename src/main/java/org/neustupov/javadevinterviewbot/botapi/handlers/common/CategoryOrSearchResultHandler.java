package org.neustupov.javadevinterviewbot.botapi.handlers.common;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.handlers.filldata.FillSearchHandler;
import org.neustupov.javadevinterviewbot.botapi.handlers.lists.CategoryListsHandler;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.model.menu.Category;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Хендлер выбора действия для кнопки возврата
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryOrSearchResultHandler implements InputMessageHandler {

  /**
   * Хендлер списка категорий
   */
  CategoryListsHandler categoryListsHandler;

  /**
   * Хендлер заполнения результатов поиска
   */
  FillSearchHandler fillSearchHandler;

  /**
   * Кеш данных пользователя
   */
  UserDataCache userDataCache;

  public CategoryOrSearchResultHandler(
      CategoryListsHandler categoryListsHandler,
      FillSearchHandler fillSearchHandler,
      UserDataCache userDataCache) {
    this.categoryListsHandler = categoryListsHandler;
    this.fillSearchHandler = fillSearchHandler;
    this.userDataCache = userDataCache;
  }

  /**
   * В зависимости от текущего состояния передаёт работу в соответствующий хендлер
   *
   * @param message Сообщение
   * @return SendMessage
   */
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

  /**
   * Возвращает сстояние, соответствующее хендлеру
   *
   * @return BotState
   */
  @Override
  public BotState getHandlerName() {
    return BotState.CATEGORY_OR_SEARCH_RESULT;
  }
}
