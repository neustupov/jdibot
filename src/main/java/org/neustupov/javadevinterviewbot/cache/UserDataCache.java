package org.neustupov.javadevinterviewbot.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDataCache implements DataCache {

  Map<Integer, Stack<BotState>> stateMap = new HashMap<>();
  Map<Integer, UserContext> userContextMap = new HashMap<>();

  public void setUserCurrentBotState(int userId, BotState botState) {
    Stack<BotState> userStateStack = stateMap.get(userId);
    if (userStateStack == null) {
      userStateStack = new Stack<>();
      userStateStack.push(botState);
      stateMap.put(userId, userStateStack);
    } else {
      userStateStack.push(botState);
    }
  }

  public BotState getUserCurrentBotState(int userId) {
    Stack<BotState> botStateStack = stateMap.get(userId);
    BotState botState = BotState.SHOW_START_MENU;
    if (botStateStack != null) {
      BotState curBotState = botStateStack.peek();
      if (curBotState != null) {
        botState = curBotState;
      }
    }
    return botState;
  }

  public BotState getPreviousUserBotState(int userId) {
    Stack<BotState> botStateStack = stateMap.get(userId);
    BotState botState = BotState.SHOW_START_MENU;
    if (botStateStack != null && !botStateStack.empty()) {
      botStateStack.pop();
      BotState curBotState = null;
      if (!botStateStack.empty()) {
        curBotState = botStateStack.peek();
      }
      if (curBotState != null) {
        botState = curBotState;
      }
    }
    return botState;
  }

  public void cleanStates(int userId) {
    stateMap.put(userId, new Stack<>());
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
