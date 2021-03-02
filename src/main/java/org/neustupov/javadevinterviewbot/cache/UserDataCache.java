package org.neustupov.javadevinterviewbot.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.model.RangePair;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.neustupov.javadevinterviewbot.repository.UserContextRepository;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDataCache implements DataCache {

  UserContextRepository contextRepository;

  Map<Long, BotState> stateMap = new HashMap<>();


  public UserDataCache(
      UserContextRepository contextRepository) {
    this.contextRepository = contextRepository;
  }

  public void setUserCurrentBotState(long userId, BotState botState) {
    stateMap.put(userId, botState);
  }

  public BotState getUserCurrentBotState(long userId) {
    return stateMap.get(userId);
  }

  public void setUserLevel(long userId, Level level){
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setLevel(level);
      contextRepository.save(userContext);
    });
  }

  public void setCategory(long userId, Category category) {
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setCategory(category);
      contextRepository.save(userContext);
    });
  }

  public void setRoute(long userId, String route){
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setRoute(route);
      contextRepository.save(userContext);
    });
  }

  public void setSearchField(long userId, String searchString){
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setSearchField(searchString);
      contextRepository.save(userContext);
    });
  }

  public void setRange(long userId, RangePair rangePair){
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setRange(rangePair);
      contextRepository.save(userContext);
    });
  }

  public void cleanStates(long userId) {
    stateMap.put(userId, null);
  }

  public void cleanSearch(long userId) {
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setSearchField("");
      contextRepository.save(userContext);
    });
  }

  @Override
  public void cleanRange(long userId) {
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setRange(null);
      contextRepository.save(userContext);
    });
  }

  @Override
  public void cleanCategory(long userId) {
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setCategory(null);
      contextRepository.save(userContext);
    });
  }

  @Override
  public void cleanLevel(long userId) {
    contextRepository.findById(userId).ifPresent(userContext -> {
      userContext.setLevel(null);
      contextRepository.save(userContext);
    });
  }

  @Override
  public void cleanAll(long userId) {
    cleanRange(userId);
    cleanCategory(userId);
    cleanLevel(userId);
    cleanStates(userId);
    cleanSearch(userId);
  }

  @Override
  public UserContext getUserContext(long userId) {
    Optional<UserContext> userContext = contextRepository.findById(userId);
    if (!userContext.isPresent()) {
      setUserContext(new UserContext(userId));
    }
    return contextRepository.findById(userId).orElse(new UserContext());
  }

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
