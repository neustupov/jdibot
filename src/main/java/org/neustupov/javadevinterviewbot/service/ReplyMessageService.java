package org.neustupov.javadevinterviewbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class ReplyMessageService {

  private LocaleMessageService localeMessageService;

  public ReplyMessageService(
      LocaleMessageService localeMessageService) {
    this.localeMessageService = localeMessageService;
  }

  public SendMessage getReplyMessage(Long chat_id, String replyMessage) {
    return SendMessage.builder().chatId(chat_id.toString()).text(localeMessageService.getMessage(replyMessage)).build();
  }

  public String getReplyText(String replyMessage) {
    return localeMessageService.getMessage(replyMessage);
  }

  public String getReplyText(String replyMessage, Object... args) {
    return localeMessageService.getMessage(replyMessage, args);
  }

  public SendMessage getReplyMessage(Long chat_id, String replyMessage, Object... args) {
    return SendMessage.builder().chatId(chat_id.toString()).text(localeMessageService.getMessage(replyMessage, args)).build();
  }
}
