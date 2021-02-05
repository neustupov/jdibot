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
public class LevelMenuHandler implements InputMessageHandler {

  private ReplyMessageService replyMessageService;

  public LevelMenuHandler(
      ReplyMessageService replyMessageService) {
    this.replyMessageService = replyMessageService;
  }

  @Override
  public SendMessage handle(Message message) {
    return processUsersInput(message);
  }

  private SendMessage processUsersInput(Message message){
    long chatId = message.getChatId();
    SendMessage replyToUser = replyMessageService.getReplyMessage(chatId, "reply.level");
    Map<String, List<String>> buttonNames = getButtonNames();
    replyToUser.setReplyMarkup(getInlineMessageButtons(buttonNames));
    return replyToUser;
  }

  private Map<String, List<String>> getButtonNames(){
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
