package org.neustupov.javadevinterviewbot.botapi.handlers.menu;

import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.BUTTONS;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Buttons.*;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.CALLBACKS;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
public class CategoryMenuHandler implements InputMessageHandler {

  //TODO сообщения должен готовить ResponseMessageCreator вынести все туда
  ReplyMessageService replyMessageService;
  ButtonMaker buttonMaker;

  public CategoryMenuHandler(
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
    SendMessage replyToUser = replyMessageService.getReplyMessage(chatId, "reply.category");
    replyToUser.setReplyMarkup(buttonMaker.getInlineMessageButtons(getButtonNames(), true));
    return replyToUser;
  }

  private Map<String, String> getButtonNames() {
    Map<String, String> buttonMap = new LinkedHashMap<>();
    buttonMap.put(OOP, OOP_CATEGORY_BUTTON);
    buttonMap.put(COLLECTIONS, COLLECTIONS_CATEGORY_BUTTON);
    buttonMap.put(PATTERNS, PATTERNS_CATEGORY_BUTTON);
    return buttonMap;
  }

  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_CATEGORY_MENU;
  }
}
