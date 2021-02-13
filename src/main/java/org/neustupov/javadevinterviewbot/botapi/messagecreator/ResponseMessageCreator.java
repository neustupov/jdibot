package org.neustupov.javadevinterviewbot.botapi.messagecreator;

import static org.neustupov.javadevinterviewbot.botapi.handlers.filldata.PaginationHandler.Pagination.NEXT;
import static org.neustupov.javadevinterviewbot.botapi.handlers.filldata.PaginationHandler.Pagination.PREVIOUS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.model.RangePair;
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

  public SendMessage getMessage(List<Question> qList, long chatId, int userId, String pagination) {
    SendMessage sendMessage = replyMessageService
        .getReplyMessage(chatId, "reply.empty-search-result");
    RangePair range = dataCache.getUserContext(userId).getRange();
    if (qList == null || qList.isEmpty()) {
      dataCache.setUserCurrentBotState(userId, BotState.FILLING_SEARCH);
    } else {
      List<Question> qListSelected = new ArrayList<>(qList);
      if (qList.size() > maxObjects) {
        if (range == null) {
          RangePair rangePair = RangePair.builder().from(0).to(maxObjects).build();
          qListSelected = getCurrentList(userId, qList, rangePair);
        } else {
          qListSelected = getCurrentList(userId, qList,
              getNewRange(qList.size(), range, pagination));
        }
      }
      sendMessage.setText(parseQuestions(qListSelected));
    }//TODO добавить кнопки для пагинации <- и ->
    RangePair newRange = dataCache.getUserContext(userId).getRange();
    if (newRange != null) {
      boolean next = false;
      boolean previous = false;
      if (newRange.getFrom() != null && newRange.getFrom() > 0) {
        previous = true;
      }
      if (qList != null && ((newRange.getTo() != null && qList.size() > newRange.getTo()))) {
        next = true;
      }
      sendMessage.setReplyMarkup(buttonMaker.getPaginationButton(previous, next));
    }
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

  private List<Question> getCurrentList(int userId, List<Question> qList, RangePair rangePair) {
    dataCache.getUserContext(userId).setRange(rangePair);
    return qList.stream()
        .skip(rangePair.getFrom())
        .limit(rangePair.getTo() - rangePair.getFrom()).collect(Collectors.toList());
  }

  private RangePair getNewRange(Integer listSize, RangePair rangePair, String pagination) {
    int from = rangePair.getFrom();
    int to = rangePair.getTo();
    switch (pagination) {
      case NEXT:
        from = from + maxObjects;
        int tempTo = to + maxObjects;
        if (tempTo <= listSize) {
          to = tempTo;
        } else {
          to = listSize;
        }
        break;
      case PREVIOUS:
        int tempFrom = from - maxObjects;
        if (tempFrom >= 0) {
          from = tempFrom;
        } else {
          from = 0;
        }
        to = to - maxObjects;
        break;
    }
    return RangePair.builder().from(from).to(to).build();
  }
}
