package org.neustupov.javadevinterviewbot.cache;

import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.model.RangePair;
import org.neustupov.javadevinterviewbot.model.UserContext;

public interface DataCache {

  void setUserCurrentBotState(long userId, BotState botState);

  BotState getUserCurrentBotState(long userId);

  void setUserContext(UserContext userContext);

  UserContext getUserContext(long userId);

  void setSearchField(long userId, String searchString);

  void setUserLevel(long userId, Level level);

  void setCategory(long userId, Category category);

  void setRoute(long userId, String route);

  void setRange(long userId, RangePair rangePair);

  void cleanSearch(long userId);

  void cleanStates(long userId);

  void cleanRange(long userId);

  void cleanCategory(long userId);

  void cleanLevel(long userId);

  void cleanAll(long userId);
}
