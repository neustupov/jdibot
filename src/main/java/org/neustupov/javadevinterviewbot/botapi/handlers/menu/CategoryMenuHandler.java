package org.neustupov.javadevinterviewbot.botapi.handlers.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class CategoryMenuHandler implements InputMessageHandler {

  private ReplyMessageService replyMessageService;

  public CategoryMenuHandler(
      ReplyMessageService replyMessageService) {
    this.replyMessageService = replyMessageService;
  }

  @Override
  public SendMessage handle(Message message) {
    return processUsersInput(message);
  }

  private SendMessage processUsersInput(Message message){
    long chatId = message.getChatId();
    SendMessage replyToUser = replyMessageService.getReplyMessage(chatId, "reply.category");
    Map<String, List<String>> buttonNames = getButtonNames();
    replyToUser.setReplyMarkup(getInlineMessageButtons(buttonNames, true));
    return replyToUser;
  }

  private Map<String, List<String>> getButtonNames(){
    List<String> buttons = new ArrayList<>();
    buttons.add("ООП");
    buttons.add("Коллекции");
    buttons.add("Паттерны");
    List<String> callbacks = new ArrayList<>();
    callbacks.add("buttonOOP");
    callbacks.add("buttonCollections");
    callbacks.add("buttonPatterns");
    Map<String, List<String>> buttonsMap = new HashMap<>();
    buttonsMap.put("buttons", buttons);
    buttonsMap.put("callbacks", callbacks);
    return buttonsMap;
  }

  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_CATEGORY_MENU;
  }
}
