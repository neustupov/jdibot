package org.neustupov.javadevinterviewbot.botapi.handlers.menu.springmenu;

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
 * Хендлер меню категории Spring
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpringCategoryMenuHandler implements InputMessageHandler {

  /**
   * Класс, собирающий сообщение
   */
  ResponseMessageCreator responseMessageCreator;

  public SpringCategoryMenuHandler(
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
    return responseMessageCreator
        .getStringMap(SPRING_PART_1.toString(), SPRING_PART_1_BUTTON.toString(),
            SPRING_PART_2.toString(), SPRING_PART_2_BUTTON.toString(),
            SPRING_PART_3.toString(), SPRING_PART_3_BUTTON.toString());
  }

  /**
   * Возвращает сстояние, соответствующее хендлеру
   *
   * @return BotState
   */
  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_SPRING_CATEGORY_MENU;
  }
}
