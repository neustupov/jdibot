package org.neustupov.javadevinterviewbot.botapi.handlers.menu.springmenu;

import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Buttons.*;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.*;

import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.messagecreator.ResponseMessageCreator;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpringCategoryMenuHandler implements InputMessageHandler {

  ResponseMessageCreator responseMessageCreator;

  public SpringCategoryMenuHandler(
      ResponseMessageCreator responseMessageCreator) {
    this.responseMessageCreator = responseMessageCreator;
  }

  @Override
  public SendMessage handle(Message message) {
    return processUsersInput(message);
  }

  private SendMessage processUsersInput(Message message) {
    long chatId = message.getChatId();
    return responseMessageCreator
        .getSimpleMessageWithButtons(chatId, "reply.category", getButtonNames());
  }

  private Map<String, String> getButtonNames() {
    return responseMessageCreator.getStringMap(SPRING_PART_1, SPRING_PART_1_BUTTON,
        SPRING_PART_2, SPRING_PART_2_BUTTON,
        SPRING_PART_3, SPRING_PART_3_BUTTON);
  }

  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_SPRING_CATEGORY_MENU;
  }
}
