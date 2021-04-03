package org.neustupov.javadevinterviewbot.botapi.handlers;

import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Интерфейс хендлеров
 */
public interface InputMessageHandler {

  /**
   * Обрабатывает входящее сообщение
   *
   * @param message Входящее сообщение
   * @return SendMessage
   */
  SendMessage handle(Message message);

  /**
   * Возвращает сстояние, соответствующее хендлеру
   *
   * @return BotState
   */
  BotState getHandlerName();
}
