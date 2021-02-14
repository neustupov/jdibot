package org.neustupov.javadevinterviewbot.botapi.messagecreator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseMessageCreator {

  @Value("${app.search.pagination.max}")
  private Integer maxObjects;

  ReplyMessageService replyMessageService;
  ButtonMaker buttonMaker;
  DataCache dataCache;
  PaginationService paginationService;

  //TODO тут либо делаем методы под каждый хендлер, либо разносим все по стратегиям - сначала собираем сообщения
  //потом цепляем кнопки

  public ResponseMessageCreator(
      ReplyMessageService replyMessageService,
      ButtonMaker buttonMaker, DataCache dataCache,
      PaginationService paginationService) {
    this.replyMessageService = replyMessageService;
    this.buttonMaker = buttonMaker;
    this.dataCache = dataCache;
    this.paginationService = paginationService;
  }

  public SendMessage getSimplyMessage(long chatId, String replyMessage, BotState state,
      boolean backToStartMenuButton) {
    dataCache.setUserCurrentBotState((int) chatId, state);
    SendMessage replyToUser = replyMessageService.getReplyMessage(chatId, replyMessage);
    if (backToStartMenuButton) {
      replyToUser.setReplyMarkup(buttonMaker.getBackToStartMenuButton());
    }
    return replyToUser;
  }

  public SendMessage getSimpleMessageWithButtons(Long chatId, String message,
      Map<String, String> buttonNames) {
    BotState botState = dataCache.getUserCurrentBotState(chatId.intValue());
    SendMessage replyToUser = replyMessageService.getReplyMessage(chatId, message);
    replyToUser.setReplyMarkup(buttonMaker.getInlineMessageButtons(buttonNames, botState));
    return replyToUser;
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
          qListSelected = paginationService.getCurrentList(userId, qList, rangePair);
        } else {
          qListSelected = paginationService.getCurrentList(userId, qList,
              paginationService.getNewRange(qList.size(), range, pagination));
        }
      }
      sendMessage.setText(parseQuestions(qListSelected));
    }
    RangePair newRange = dataCache.getUserContext(userId).getRange();
    boolean next = false;
    boolean previous = false;
    if (newRange != null) {
      next = paginationService.addNextButton(qList, newRange);
      previous = paginationService.addPreviousButton(newRange);
    }
    BotState userCurrentBotState = dataCache.getUserCurrentBotState(userId);
    sendMessage
        .setReplyMarkup(buttonMaker.getPaginationButton(previous, next, userCurrentBotState));

    return sendMessage;
  }

  private String parseQuestions(List<Question> qList) {
    StringBuffer sb = new StringBuffer();
    qList.forEach(q -> {
      sb.append(q.getLink());
      sb.append(" ");
      sb.append(q.getSmallDescription());
      sb.append("\n");
    });
    return sb.toString();
  }

  public Map<String, String> getStringMap(String first, String firstCallback,
      String second, String secondCallback, String third,
      String thirdCallback) {
    Map<String, String> buttonMap = new LinkedHashMap<>();
    buttonMap.put(first, firstCallback);
    buttonMap.put(second, secondCallback);
    buttonMap.put(third, thirdCallback);
    return buttonMap;
  }
}
