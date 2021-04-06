package org.neustupov.javadevinterviewbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Сервис создания сообщений ответов
 */
@Service
public class ReplyMessageService {

  /**
   * Сервис локализации сообщений
   */
  private LocaleMessageService localeMessageService;

  public ReplyMessageService(
      LocaleMessageService localeMessageService) {
    this.localeMessageService = localeMessageService;
  }

  /**
   * Создает сообщение ответа
   *
   * @param chatId chatId
   * @param replyMessage Сообщение
   * @return SendMessage
   */
  public SendMessage getReplyMessage(Long chatId, String replyMessage) {
    return SendMessage.builder().chatId(chatId.toString())
        .text(localeMessageService.getMessage(replyMessage)).build();
  }

  /**
   * Создает сообщение ответа для вопроса
   *
   * @param chatId chatId
   * @param replyMessage Сообщение
   * @return SendMessage
   */
  public SendMessage getReplyMessageForQuestion(Long chatId, String replyMessage) {
    return SendMessage.builder().chatId(chatId.toString()).text(replyMessage).build();
  }

  /**
   * Создает сообщение ответа для вопроса
   *
   * @param chatId chatId
   * @param replyMessage Сообщение
   * @param args Дополнительные аргументы
   * @return SendMessage
   */
  SendMessage getReplyMessage(Long chatId, String replyMessage, Object... args) {
    return SendMessage.builder().chatId(chatId.toString())
        .text(localeMessageService.getMessage(replyMessage, args)).build();
  }

  /**
   * Возвращает локализованное сообщение
   *
   * @param replyMessage Сообщение
   * @return Локализованное сообщение
   */
  String getReplyText(String replyMessage) {
    return localeMessageService.getMessage(replyMessage);
  }

  /**
   * Возвращает локализованное сообщение
   *
   * @param replyMessage Сообщение
   * @param args Дополнительные аргументы
   * @return Локализованное сообщение
   */
  String getReplyText(String replyMessage, Object... args) {
    return localeMessageService.getMessage(replyMessage, args);
  }
}
