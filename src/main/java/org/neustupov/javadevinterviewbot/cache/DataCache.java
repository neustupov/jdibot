package org.neustupov.javadevinterviewbot.cache;

import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.model.menu.Category;
import org.neustupov.javadevinterviewbot.model.menu.Level;
import org.neustupov.javadevinterviewbot.model.RangePair;
import org.neustupov.javadevinterviewbot.model.UserContext;

/**
 * Интерфейс кеша данных пользователя
 */
public interface DataCache {

  /**
   * Устанавливает состояние бота для пользователя
   *
   * @param userId userId пользователя
   * @param botState Состояние
   */
  void setUserCurrentBotState(long userId, BotState botState);

  /**
   * Возвращает состояние бота для пользователя
   *
   * @param userId userId пользователя
   * @return Состояние
   */
  BotState getUserCurrentBotState(long userId);

  /**
   * Устанавливает контекст пользователя
   *
   * @param userContext Контекст пользователя
   */
  void setUserContext(UserContext userContext);

  /**
   * Возвращает контекст пользователя по Id
   *
   * @param userId userId пользователя
   * @return Контекст пользователя
   */
  UserContext getUserContext(long userId);

  /**
   * Устанавливает информацию поиска
   *
   * @param userId userId пользователя
   * @param searchString Тескт для поиска
   */
  void setSearchField(long userId, String searchString);

  /**
   * Устанавливает уровень
   *
   * @param userId userId пользователя
   * @param level Уровень
   */
  void setUserLevel(long userId, Level level);

  /**
   * Устанавливает категорию
   *
   * @param userId userId пользователя
   * @param category Категория
   */
  void setCategory(long userId, Category category);

  /**
   * Устанавливает направление смещения при пагинации
   *
   * @param userId userId пользователя
   * @param route Направление смещения при пагинации
   */
  void setRoute(long userId, String route);

  /**
   * Устанавливает диапазон
   *
   * @param userId userId пользователя
   * @param rangePair Диапазон
   */
  void setRange(long userId, RangePair rangePair);

  /**
   * Чистит поиск пользователя
   *
   * @param userId userId пользователя
   */
  void cleanSearch(long userId);

  /**
   * Чистит состояние бота для пользователя
   *
   * @param userId userId пользователя
   */
  void cleanStates(long userId);

  /**
   * Чистит диапазон пагинации для пользователя
   *
   * @param userId userId пользователя
   */
  void cleanRange(long userId);

  /**
   * Чистит категорию пользователя
   *
   * @param userId userId пользователя
   */
  void cleanCategory(long userId);

  /**
   * Чистит уровень пользователя
   *
   * @param userId userId пользователя
   */
  void cleanLevel(long userId);

  /**
   * Чистит все
   *
   * @param userId userId пользователя
   */
  void cleanAll(long userId);
}
