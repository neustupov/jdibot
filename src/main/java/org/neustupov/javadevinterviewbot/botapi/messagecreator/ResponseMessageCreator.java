package org.neustupov.javadevinterviewbot.botapi.messagecreator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.neustupov.javadevinterviewbot.model.buttons.ButtonCallbacks;
import org.neustupov.javadevinterviewbot.model.menu.Category;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.model.RangePair;
import org.neustupov.javadevinterviewbot.service.ReplyMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Создает сообщения, добавляет кнопки пагинации и возврата
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseMessageCreator {

  /**
   * Количество вопросов в сообщении
   */
  @Value("${app.pagination}")
  private Integer maxObjects;

  /**
   * Сервис ответов
   */
  final ReplyMessageService replyMessageService;

  /**
   * Класс, создающий кнопки
   */
  final ButtonMaker buttonMaker;

  /**
   * Кеш данных пользователя
   */
  final DataCache dataCache;

  /**
   * Сервис пагинации
   */
  final PaginationService paginationService;

  public ResponseMessageCreator(
      ReplyMessageService replyMessageService,
      ButtonMaker buttonMaker, DataCache dataCache,
      PaginationService paginationService) {
    this.replyMessageService = replyMessageService;
    this.buttonMaker = buttonMaker;
    this.dataCache = dataCache;
    this.paginationService = paginationService;
  }

  /**
   * Создает сообщение с кнопками возврата
   *
   * @param chatId chatId
   * @param replyMessage Текст сообщения
   * @param state Состояние
   * @param backButton Кнопка возврата
   * @return SendMessage
   */
  public SendMessage getSimplyMessage(long chatId, String replyMessage, BotState state,
      ButtonCallbacks backButton) {
    dataCache.setUserCurrentBotState((int) chatId, state);
    SendMessage replyToUser;
    if (state.equals(BotState.SHOW_QUESTION)) {
      replyToUser = replyMessageService.getReplyMessageForQuestion(chatId, replyMessage);
    } else {
      replyToUser = replyMessageService.getReplyMessage(chatId, replyMessage);
    }
    if (backButton != null) {
      switch (backButton) {
        case BACK_TO_START_MENU_BUTTON:
          replyToUser.setReplyMarkup(buttonMaker.getBackToStartMenuButton());
          break;
        case BACK_BUTTON:
          replyToUser.setReplyMarkup(buttonMaker.getBackButton());
          break;
      }
    }
    return replyToUser;
  }

  /**
   * Создает сообщение с кнопками
   *
   * @param chatId chatId
   * @param replyMessage Текст сообщения
   * @param buttonNames Мапа с названиями кнопок\колбеками
   * @return SendMessage
   */
  public SendMessage getSimpleMessageWithButtons(Long chatId, String replyMessage,
      Map<String, String> buttonNames) {
    BotState botState = dataCache.getUserCurrentBotState(chatId.intValue());
    SendMessage replyToUser = replyMessageService.getReplyMessage(chatId, replyMessage);
    replyToUser.setReplyMarkup(buttonMaker.getInlineMessageButtons(buttonNames, botState));
    return replyToUser;
  }

  /**
   * Создает сообщение с кнопками пагинации
   *
   * @param qList Список вопросов
   * @param chatId chatId
   * @param userId userId
   * @param pagination Направление смещения
   * @return SendMessage
   */
  public SendMessage getMessage(List<Question> qList, Long chatId, int userId, String pagination) {
    SendMessage sendMessage = replyMessageService
        .getReplyMessage(chatId, "reply.empty-search-result");
    UserContext userContext = dataCache.getUserContext(userId);
    if (qList == null || qList.isEmpty()) {
      dataCache.setUserCurrentBotState(userId, BotState.FILLING_SEARCH);
    } else {
      List<Question> qListSelected = new ArrayList<>(qList);
      if (qList.size() > maxObjects) {
        RangePair rangePair;
        RangePair range = userContext.getRange();
        if (range == null) {
          rangePair = RangePair.builder().from(0).to(maxObjects).build();
          dataCache.setRange(userId, rangePair);
          qListSelected = paginationService.getCurrentList(qList, rangePair);
        } else {
          rangePair = paginationService.getNewRange(qList.size(), range, pagination);
          dataCache.setRange(userId, rangePair);
          qListSelected = paginationService.getCurrentList(qList, rangePair);
        }
      }
      sendMessage.setText(parseQuestions(userContext.getCategory(), qListSelected));
    }
    RangePair newRange = userContext.getRange();
    boolean next = false;
    boolean previous = false;
    if (newRange != null) {
      next = paginationService.addNextButton(qList, newRange);
      previous = paginationService.addPreviousButton(newRange);
    }
    BotState userCurrentBotState = dataCache.getUserCurrentBotState(chatId.intValue());
    sendMessage
        .setReplyMarkup(buttonMaker.getPaginationButton(previous, next, userCurrentBotState));

    return sendMessage;
  }

  /**
   * Собирает мапу для кнопок
   *
   * @param first Название первой кнопки
   * @param firstCallback Колбек первой кнопки
   * @param second Название второй кнопки
   * @param secondCallback Колбек второй кнопки
   * @param third Название третьей кнопки
   * @param thirdCallback Колбек третьей кнопки
   * @return Мапа названий\колбеков кнопок
   */
  public Map<String, String> getStringMap(String first, String firstCallback,
      String second, String secondCallback, String third,
      String thirdCallback) {
    Map<String, String> buttonMap = new LinkedHashMap<>();
    buttonMap.put(first, firstCallback);
    buttonMap.put(second, secondCallback);
    buttonMap.put(third, thirdCallback);
    return buttonMap;
  }

  /**
   * Формирует строку для вывода вопросов в сообщении
   *
   * @param category Категория вопросов
   * @param qList Список вопросов
   * @return String
   */
  private String parseQuestions(Category category, List<Question> qList) {
    StringBuffer sb = new StringBuffer();
    if (category != null) {
      sb.append(category.toString());
      sb.append("\n");
      sb.append("\n");
    }
    qList.forEach(q -> {
      sb.append("/q").append(q.getId());
      sb.append(" ");
      sb.append(q.getSmallDescription());
      sb.append("\n");
      sb.append("\n");
    });
    return sb.toString();
  }
}
