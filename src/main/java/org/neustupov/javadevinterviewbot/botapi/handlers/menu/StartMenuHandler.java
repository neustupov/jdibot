package org.neustupov.javadevinterviewbot.botapi.handlers.menu;

import static org.neustupov.javadevinterviewbot.model.buttons.ButtonCallbacks.*;
import static org.neustupov.javadevinterviewbot.model.buttons.ButtonNames.*;

import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.messagecreator.ResponseMessageCreator;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Хендлер главного меню
 */
@Component
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public class StartMenuHandler implements InputMessageHandler {

  /**
   * Класс, собирающий сообщение
   */
  ResponseMessageCreator responseMessageCreator;

  public StartMenuHandler(
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
        .getSimpleMessageWithButtons(message.getChatId(), "reply.menu", getButtonNames());
  }

  /**
   * Создает мапу с названиями кнопок и колбеками
   *
   * @return Мапа с названиями кнопок и колбеками
   */
  private Map<String, String> getButtonNames() {
    return responseMessageCreator.getStringMap(QUESTIONS.toString(), QUESTIONS_BUTTON.toString(),
        SEARCH.toString(), SEARCH_BUTTON.toString(),
        TESTS.toString(), TESTS_BUTTON.toString());
  }

  /**
   * Возвращает сстояние, соответствующее хендлеру
   *
   * @return BotState
   */
  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_START_MENU;
  }
}
