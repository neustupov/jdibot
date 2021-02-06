package org.neustupov.javadevinterviewbot.botapi.handlers.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LevelMenuHandler implements InputMessageHandler {

  ReplyMessageService replyMessageService;
  ButtonMaker buttonMaker;

  public LevelMenuHandler(
      ReplyMessageService replyMessageService,
      ButtonMaker buttonMaker) {
    this.replyMessageService = replyMessageService;
    this.buttonMaker = buttonMaker;
  }

  @Override
  public SendMessage handle(Message message) {
    return processUsersInput(message);
  }

  private SendMessage processUsersInput(Message message) {
    long chatId = message.getChatId();
    SendMessage replyToUser = replyMessageService.getReplyMessage(chatId, "reply.level");
    Map<String, List<String>> buttonNames = getButtonNames();
    replyToUser.setReplyMarkup(buttonMaker.getInlineMessageButtons(buttonNames, true));
    return replyToUser;
  }

  private Map<String, List<String>> getButtonNames() {
    List<String> buttons = new ArrayList<>();
    buttons.add("Junior");
    buttons.add("Middle");
    buttons.add("Senior");
    List<String> callbacks = new ArrayList<>();
    callbacks.add("buttonJunior");
    callbacks.add("buttonMiddle");
    callbacks.add("buttonSenior");
    Map<String, List<String>> buttonsMap = new HashMap<>();
    buttonsMap.put("buttons", buttons);
    buttonsMap.put("callbacks", callbacks);
    return buttonsMap;
  }

  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_LEVEL_MENU;
  }
}
