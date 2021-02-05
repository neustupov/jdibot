package org.neustupov.javadevinterviewbot.cache;

import java.util.HashMap;
import java.util.Map;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.springframework.stereotype.Component;

@Component
public class UserDataCache implements DataCache {

  private Map<Integer, BotState> stateMap = new HashMap<>();
  private Map<Integer, UserContext> userContextMap = new HashMap<>();

  public void setUserCurrentBotState(int userId, BotState botState) {
    stateMap.put(userId, botState);
  }

  public BotState getUserCurrentBotState(int userId) {
    BotState botState = stateMap.get(userId);
    if (botState == null) {
      botState = BotState.SHOW_START_MENU;
    }
    return botState;
  }

  @Override
  public void setUserContext(int userId, UserContext userContext) {
    userContextMap.put(userId, userContext);
  }

  @Override
  public UserContext getUserContext(int userId) {
    UserContext userContext = userContextMap.get(userId);
    if (userContext == null){
      userContext = new UserContext();
      userContextMap.put(userId, userContext);
    }
    return userContext;
  }
}
