package org.neustupov.javadevinterviewbot.botapi.handlers.search;

import static org.neustupov.javadevinterviewbot.model.buttons.ButtonCallbacks.BACK_TO_START_MENU_BUTTON;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.messagecreator.ResponseMessageCreator;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Хендлер поиска
 */
@Component
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public class SearchHandler implements InputMessageHandler {

  /**
   * Класс, собирающий сообщение
   */
  ResponseMessageCreator responseMessageCreator;

  public SearchHandler(
      ResponseMessageCreator responseMessageCreator) {
    this.responseMessageCreator = responseMessageCreator;
  }

  /**
   * Обрабатывает входящее сообщение
   *
   * @param message Входящее сообщение
   * @return SendMessage
   */
  @Override
  public SendMessage handle(Message message) {
    return processUsersInput(message);
  }

  /**
   * Создает сообщение-ответ
   *
   * @param message Входящее сообщение
   * @return Сообщение-ответ
   */
  private SendMessage processUsersInput(Message message) {
    return responseMessageCreator
        .getSimplyMessage(message.getChatId(), "reply.search", BotState.FILLING_SEARCH,
            BACK_TO_START_MENU_BUTTON);
  }

  /**
   * Возвращает сстояние, соответствующее хендлеру
   *
   * @return BotState
   */
  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_SEARCH;
  }
}
