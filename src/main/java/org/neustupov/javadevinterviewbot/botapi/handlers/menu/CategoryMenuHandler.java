package org.neustupov.javadevinterviewbot.botapi.handlers.menu;

import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Buttons.*;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.*;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.messagecreator.ResponseMessageCreator;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Хендлер меню категорий
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryMenuHandler implements InputMessageHandler {

  /**
   * Класс, собирающий сообщение
   */
  ResponseMessageCreator responseMessageCreator;

  public CategoryMenuHandler(
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
        .getSimpleMessageWithButtons(message.getChatId(), "reply.category", getButtonNames());
  }

  /**
   * Создает мапу с названиями кнопок и колбеками
   *
   * @return Мапа с названиями кнопок и колбеками
   */
  private Map<String, String> getButtonNames() {
    Map<String, String> resultMap = new HashMap<>();
    resultMap.put(OOP, OOP_CATEGORY_BUTTON);
    resultMap.put(COLLECTIONS, COLLECTIONS_CATEGORY_BUTTON);
    resultMap.put(PATTERNS, PATTERNS_CATEGORY_BUTTON);
    resultMap.put(SPRING, SPRING_BUTTON);
    return resultMap;
  }

  /**
   * Возвращает сстояние, соответствующее хендлеру
   *
   * @return BotState
   */
  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_CATEGORY_MENU;
  }
}
