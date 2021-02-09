package org.neustupov.javadevinterviewbot.botapi.handlers;

import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface InputMessageHandler {

  SendMessage handle(Message message);
  BotState getHandlerName();
}
