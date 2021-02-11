package org.neustupov.javadevinterviewbot.botapi.messagecreator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.service.ReplyMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseMessageCreator {

  @Value("${app.search.pagination.max}")
  private Integer maxObjects;

  ReplyMessageService replyMessageService;
  ButtonMaker buttonMaker;
  DataCache dataCache;

  //TODO тут либо делаем методы под каждый хендлер, либо разносим все по стратегиям - сначала собираем сообщения
  //потом цепляем кнопки


  public ResponseMessageCreator(
      ReplyMessageService replyMessageService,
      ButtonMaker buttonMaker, DataCache dataCache) {
    this.replyMessageService = replyMessageService;
    this.buttonMaker = buttonMaker;
    this.dataCache = dataCache;
  }

  public SendMessage getMessage(List<Question> qList, long chatId, int userId, Integer from,
      Integer to) {
    SendMessage sendMessage = replyMessageService
        .getReplyMessage(chatId, "reply.empty-search-result");
    if (qList == null || qList.isEmpty()) {
      dataCache.setUserCurrentBotState(userId, BotState.FILLING_SEARCH);
    } else {
      List<Question> qListSelected = new ArrayList<>(qList);
      if (qList.size() > maxObjects) {
        if (from == null && to == null) {
          qListSelected = getCurrentList(userId, qList, 0, maxObjects);
        } else {
          qListSelected = getCurrentList(userId, qList, from, to);
        }
      }
      sendMessage.setText(parseQuestions(qListSelected));
    }
    //TODO добавить кнопки для пагинации <- и ->
    sendMessage.setReplyMarkup(buttonMaker.getBackFromSearchButtons());
    return sendMessage;
  }

  public String parseQuestions(List<Question> qList) {
    StringBuffer sb = new StringBuffer();
    qList.forEach(q -> {
      sb.append(q.getLink());
      sb.append(" ");
      sb.append(q.getSmallDescription());
      sb.append("\n");
    });
    return sb.toString();
  }

  private List<Question> getCurrentList(int userId, List<Question> qList, int from, int to) {
    dataCache.getUserContext(userId).setFrom(from);
    dataCache.getUserContext(userId).setTo(to);
    return qList.stream()
        .skip(from)
        .limit(to - from).collect(Collectors.toList());
  }
}