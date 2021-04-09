package org.neustupov.javadevinterviewbot.cache;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.model.menu.Category;
import org.neustupov.javadevinterviewbot.model.menu.Level;
import org.neustupov.javadevinterviewbot.model.RangePair;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.neustupov.javadevinterviewbot.model.UserState;
import org.neustupov.javadevinterviewbot.repository.UserContextRepository;
import org.neustupov.javadevinterviewbot.repository.UserStateRepository;
import org.springframework.stereotype.Component;

/**
 * Кеш пользователя
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDataCache implements DataCache {

  /**
   * Репозиторий контекстов пользователей
   */
  UserContextRepository contextRepository;

  /**
   * Репозирорий состояний пользователей
   */
  UserStateRepository userStateRepository;


  public UserDataCache(
      UserContextRepository contextRepository,
      UserStateRepository userStateRepository) {
    this.contextRepository = contextRepository;
    this.userStateRepository = userStateRepository;
  }

  /**
   * Устанавливает состояние бота для пользователя
   *
   * @param userId userId пользователя
   * @param botState Состояние
   */
  public void setUserCurrentBotState(long userId, BotState botState) {
    Optional<UserState> userStateOptional = userStateRepository.findById(userId);
    if (userStateOptional.isPresent()) {
      UserState userState = userStateOptional.get();
      userState.setBotState(botState);
      userStateRepository.save(userState);
    } else {
      userStateRepository.save(new UserState(userId));
    }
  }

  /**
   * Возвращает состояние бота для пользователя
   *
   * @param userId userId пользователя
   * @return Состояние
   */
  public BotState getUserCurrentBotState(long userId) {
    Optional<UserState> userState = userStateRepository.findById(userId);
    return userState.map(UserState::getBotState).orElse(null);
  }

  /**
   * Устанавливает уровень
   *
   * @param userId userId пользователя
   * @param level Уровень
   */
  public void setUserLevel(long userId, Level level) {
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setLevel(level);
      contextRepository.save(userContext);
    });
  }

  /**
   * Устанавливает категорию
   *
   * @param userId userId пользователя
   * @param category Категория
   */
  public void setCategory(long userId, Category category) {
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setCategory(category);
      contextRepository.save(userContext);
    });
  }

  /**
   * Устанавливает направление смещения при пагинации
   *
   * @param userId userId пользователя
   * @param route Направление смещения при пагинации
   */
  public void setRoute(long userId, String route) {
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setRoute(route);
      contextRepository.save(userContext);
    });
  }

  /**
   * Устанавливает информацию поиска
   *
   * @param userId userId пользователя
   * @param searchString Тескт для поиска
   */
  public void setSearchField(long userId, String searchString) {
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setSearchField(searchString);
      contextRepository.save(userContext);
    });
  }

  /**
   * Устанавливает диапазон
   *
   * @param userId userId пользователя
   * @param rangePair Диапазон
   */
  public void setRange(long userId, RangePair rangePair) {
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setRange(rangePair);
      contextRepository.save(userContext);
    });
  }

  /**
   * Чистит состояние бота для пользователя
   *
   * @param userId userId пользователя
   */
  public void cleanStates(long userId) {
    userStateRepository.findById(userId).ifPresent(userState -> {
      userState.setBotState(null);
      userStateRepository.save(userState);
    });
  }

  /**
   * Чистит поиск пользователя
   *
   * @param userId userId пользователя
   */
  public void cleanSearch(long userId) {
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setSearchField("");
      contextRepository.save(userContext);
    });
  }

  /**
   * Чистит диапазон пагинации для пользователя
   *
   * @param userId userId пользователя
   */
  @Override
  public void cleanRange(long userId) {
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setRange(null);
      contextRepository.save(userContext);
    });
  }

  /**
   * Чистит категорию пользователя
   *
   * @param userId userId пользователя
   */
  @Override
  public void cleanCategory(long userId) {
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setCategory(null);
      contextRepository.save(userContext);
    });
  }

  /**
   * Чистит уровень пользователя
   *
   * @param userId userId пользователя
   */
  @Override
  public void cleanLevel(long userId) {
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setLevel(null);
      contextRepository.save(userContext);
    });
  }

  /**
   * Чистит все
   *
   * @param userId userId пользователя
   */
  @Override
  public void cleanAll(long userId) {
    cleanRange(userId);
    cleanCategory(userId);
    cleanLevel(userId);
    cleanStates(userId);
    cleanSearch(userId);
  }

  /**
   * Возвращает контекст пользователя по Id
   *
   * @param userId userId пользователя
   * @return Контекст пользователя
   */
  @Override
  public UserContext getUserContext(long userId) {
    Optional<UserContext> userContext = contextRepository.findById(userId);
    if (!userContext.isPresent()) {
      setUserContext(new UserContext(userId));
    }
    return contextRepository.findById(userId).orElse(new UserContext());
  }

  /**
   * Устанавливает контекст пользователя
   *
   * @param userContext Контекст пользователя
   */
  @Override
  public void setUserContext(UserContext userContext) {
    UserContext context = contextRepository.findById(userContext.getUserId()).orElse(null);
    if (context != null) {
      context.setLevel(userContext.getLevel());
      context.setCategory(userContext.getCategory());
      context.setRoute(userContext.getRoute());
      context.setRange(userContext.getRange());
      context.setSearchField(userContext.getSearchField());
    }
    contextRepository.save(userContext);
  }
}
