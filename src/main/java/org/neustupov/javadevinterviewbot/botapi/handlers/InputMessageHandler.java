package org.neustupov.javadevinterviewbot.botapi.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.model.Question;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface InputMessageHandler {

  SendMessage handle(Message message);

  BotState getHandlerName();

  default String parseQuestions(List<Question> qList){
    StringBuffer sb = new StringBuffer();
    qList.forEach(q-> {
      sb.append(q.getLink());
      sb.append(" ");
      sb.append(q.getSmallDescription());
      sb.append("\n");
    });
    return sb.toString();
  }
}
