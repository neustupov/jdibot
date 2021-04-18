package org.neustupov.javadevinterviewbot.botapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Контекст бота
 */
@Component
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public class BotStateContext {

  /**
   * Мапа хендлеров
   */
  Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

  public BotStateContext(List<InputMessageHandler> messageHandlers) {
    messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
  }

  /**
   * Обрабатывает сообщение хендлером в зависимости от состояния
   *
   * @param botState Состояние
   * @param message Сообщение
   * @return Ответ
   */
  public SendMessage processInputMessage(BotState botState, Message message) {
    InputMessageHandler currentMessageHandler = findMessageHandler(botState);
    return currentMessageHandler.handle(message);
  }

  /**
   * Находит подходящий хендлер
   *
   * @param currentState Состояние
   * @return Хендлер
   */
  private InputMessageHandler findMessageHandler(BotState currentState) {
    return messageHandlers.get(currentState);
  }
}
