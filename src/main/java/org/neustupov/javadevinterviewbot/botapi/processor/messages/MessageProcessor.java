package org.neustupov.javadevinterviewbot.botapi.processor.messages;

import static org.neustupov.javadevinterviewbot.botapi.processor.messages.MessageProcessor.Commands.QUESTION;
import static org.neustupov.javadevinterviewbot.botapi.processor.messages.MessageProcessor.Commands.START;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageProcessor {

  UserDataCache userDataCache;
  BotStateContext botStateContext;

  interface Commands {
    String START = "/start";
    String QUESTION = "/q";
  }

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
      case START:
        botState = BotState.SHOW_START_MENU;
        userDataCache.cleanStates(userId);
        userDataCache.cleanSearch(userId);
        userDataCache.setUserCurrentBotState(userId, botState);
        break;
      default:
        botState = userDataCache.getUserCurrentBotState(userId);
        break;
    }

    if (inputMsg.startsWith(QUESTION)) {
      botState = BotState.SHOW_QUESTION;
      userDataCache.setUserCurrentBotState(userId, botState);
      return botStateContext.processInputMessage(botState, message);
    }

    return botStateContext.processInputMessage(botState, message);
  }
}
