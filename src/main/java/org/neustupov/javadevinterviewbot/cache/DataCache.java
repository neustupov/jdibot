package org.neustupov.javadevinterviewbot.cache;

import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.model.UserContext;

public interface DataCache {

  void setUserCurrentBotState(int userId, BotState botState);
  BotState getUserCurrentBotState(int userId);
  void setUserContext(int userId, UserContext userContext);
  UserContext getUserContext(int userId);
}
