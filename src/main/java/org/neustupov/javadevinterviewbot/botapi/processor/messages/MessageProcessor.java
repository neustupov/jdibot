package org.neustupov.javadevinterviewbot.botapi.processor.messages;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageProcessor {

  UserDataCache userDataCache;
  BotStateContext botStateContext;

  public MessageProcessor(UserDataCache userDataCache,
      BotStateContext botStateContext) {
    this.userDataCache = userDataCache;
    this.botStateContext = botStateContext;
  }

  public SendMessage handleInputMessage(Message message) {
    String inputMsg = message.getText();
    int userId = message.getFrom().getId();
    BotState botState;

    switch (inputMsg) {
      case "/start":
        botState = BotState.SHOW_START_MENU;
        userDataCache.cleanStates(userId);
        userDataCache.setUserCurrentBotState(userId, botState);
        break;
      default:
        botState = userDataCache.getUserCurrentBotState(userId);
        break;
    }

    if (inputMsg.startsWith("/q")) {
      botState = BotState.SHOW_QUESTION;
      userDataCache.setUserCurrentBotState(userId, botState);
      return botStateContext.processInputMessage(botState, message);
    }

    //userDataCache.setUserCurrentBotState(userId, botState);
    return botStateContext.processInputMessage(botState, message);
  }
}
