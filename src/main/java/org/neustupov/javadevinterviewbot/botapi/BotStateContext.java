package org.neustupov.javadevinterviewbot.botapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class BotStateContext {

  private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

  public BotStateContext(List<InputMessageHandler> messageHandlers) {
    messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
  }

  public SendMessage processInputMessage(BotState botState, Message message) {
    InputMessageHandler currentMessageHandler = findMessageHandler(botState);
    return currentMessageHandler.handle(message);
  }

  private InputMessageHandler findMessageHandler(BotState currentState) {
    return messageHandlers.get(currentState);
  }
}
