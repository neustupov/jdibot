package org.neustupov.javadevinterviewbot.botapi.handlers.menu;

import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Buttons.*;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.*;
import static org.neustupov.javadevinterviewbot.botapi.states.BotState.SHOW_START_MENU;

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
public class StartMenuHandler implements InputMessageHandler {

  ResponseMessageCreator responseMessageCreator;

  public StartMenuHandler(
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
        .getSimpleMessageWithButtons(chatId, "reply.menu", getButtonNames());
  }

  private Map<String, String> getButtonNames() {
    return responseMessageCreator.getStringMap(QUESTIONS, QUESTIONS_BUTTON,
        SEARCH, SEARCH_BUTTON,
        TESTS, TESTS_BUTTON);
  }

  @Override
  public BotState getHandlerName() {
    return SHOW_START_MENU;
  }
}
