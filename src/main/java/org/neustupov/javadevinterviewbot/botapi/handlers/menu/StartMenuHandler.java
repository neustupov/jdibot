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
public class StartMenuHandler implements InputMessageHandler {

  private ReplyMessageService replyMessageService;

  public StartMenuHandler(ReplyMessageService replyMessageService) {
    this.replyMessageService = replyMessageService;
  }

  @Override
  public SendMessage handle(Message message) {
    return processUsersInput(message);
  }

  private SendMessage processUsersInput(Message message){
    long chatId = message.getChatId();
    SendMessage replyToUser = replyMessageService.getReplyMessage(chatId, "reply.menu");
    Map<String, List<String>> buttonNames = getButtonNames();
    replyToUser.setReplyMarkup(getInlineMessageButtons(buttonNames, false));
    return replyToUser;
  }

  private Map<String, List<String>> getButtonNames(){
    List<String> buttons = new ArrayList<>();
    buttons.add("Вопросы");
    buttons.add("Поиск");
    buttons.add("Тестирование");
    List<String> callbacks = new ArrayList<>();
    callbacks.add("buttonQuestions");
    callbacks.add("buttonSearch");
    callbacks.add("buttonTest");
    Map<String, List<String>> buttonsMap = new HashMap<>();
    buttonsMap.put("buttons", buttons);
    buttonsMap.put("callbacks", callbacks);
    return buttonsMap;
  }

  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_START_MENU;
  }
}
