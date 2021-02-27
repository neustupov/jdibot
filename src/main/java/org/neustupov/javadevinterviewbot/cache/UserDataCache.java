package org.neustupov.javadevinterviewbot.cache;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDataCache implements DataCache {

  Map<Integer, BotState> stateMap = new HashMap<>();
  Map<Integer, UserContext> userContextMap = new HashMap<>();

  public void setUserCurrentBotState(int userId, BotState botState) {
    stateMap.put(userId, botState);
  }

  public BotState getUserCurrentBotState(int userId) {
    return stateMap.get(userId);
  }

  public void cleanStates(int userId) {
    stateMap.put(userId, null);
  }

  public void cleanSearch(int userId) {
    UserContext userContext = userContextMap.get(userId);
    if (userContext != null) {
      userContext.setSearchField("");
    }
  }

  @Override
  public void cleanRange(int userId) {
    UserContext userContext = userContextMap.get(userId);
    if (userContext != null) {
      userContext.setRange(null);
    }
  }

  @Override
  public void cleanCategory(int userId) {
    UserContext userContext = userContextMap.get(userId);
    if (userContext != null) {
      userContext.setCategory(null);
    }
  }

  @Override
  public void cleanLevel(int userId) {
    UserContext userContext = userContextMap.get(userId);
    if (userContext != null) {
      userContext.setLevel(null);
    }
  }

  @Override
  public void cleanAll(int userId) {
    userContextMap.put(userId, new UserContext());
  }

  @Override
  public void setUserContext(int userId, UserContext userContext) {
    userContextMap.put(userId, userContext);
  }

  @Override
  public UserContext getUserContext(int userId) {
    UserContext userContext = userContextMap.get(userId);
    if (userContext == null) {
      userContext = new UserContext();
      userContextMap.put(userId, userContext);
    }
    return userContext;
  }
}
