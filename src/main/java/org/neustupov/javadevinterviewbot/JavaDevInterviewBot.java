package org.neustupov.javadevinterviewbot;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.TelegramFacade;
import org.neustupov.javadevinterviewbot.model.BotResponseData;
import org.neustupov.javadevinterviewbot.model.MessageIdKeeper;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JavaDevInterviewBot extends TelegramWebhookBot {

  String webHookPath;
  String botUserName;
  String botToken;

  TelegramFacade telegramFacade;

  public JavaDevInterviewBot(TelegramFacade telegramFacade) {
    this.telegramFacade = telegramFacade;
  }

  @Override
  public String getBotUsername() {
    return botUserName;
  }

  @Override
  public String getBotToken() {
    return botToken;
  }

  @Override
  public String getBotPath() {
    return webHookPath;
  }

  @Override
  public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
    if ((update.getMessage() != null && update.getMessage().hasText()) || update
        .hasCallbackQuery()) {
      BotResponseData botResponseData = telegramFacade.handleUpdate(update);
      MessageIdKeeper messageIdKeeper = botResponseData.getMessageIdKeeper();
      deletePreviousMessages(messageIdKeeper);
      return botResponseData.getBotApiMethod();
    }
    return null;
  }

  //TODO Тут можно все упростить, при необходимости удаляя предыдущее сообщение как текущее-1
  private void deletePreviousMessages(MessageIdKeeper messageIdKeeper) {
    Long chatId = messageIdKeeper.getChatId();

    Integer previousMessageId = messageIdKeeper.getPreviousMessageId();
    if (previousMessageId != null && messageIdKeeper.isNeedDeletePrevious()) {

      DeleteMessage deleteMessage = new DeleteMessage();
      deleteMessage.setChatId(chatId.toString());
      deleteMessage.setMessageId(previousMessageId);
      try {
        execute(deleteMessage);
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }

      messageIdKeeper.setNeedDeletePrevious(false);
      messageIdKeeper.setPreviousMessageId(null);
    }

    Integer previousPreviousMessageId = messageIdKeeper.getPreviousPreviousMessageId();
    if (previousPreviousMessageId != null && messageIdKeeper.isNeedDeletePreviousPrevious()) {

      DeleteMessage deleteMessage = new DeleteMessage();
      deleteMessage.setChatId(chatId.toString());
      deleteMessage.setMessageId(previousPreviousMessageId + 1);
      try {
        execute(deleteMessage);
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }

      messageIdKeeper.setNeedDeletePreviousPrevious(false);
      messageIdKeeper.setPreviousPreviousMessageId(null);
    }

    Integer imageMessageId = messageIdKeeper.getImageMessageId();
    if (imageMessageId != null && messageIdKeeper.isNeedDeleteImage()) {
      DeleteMessage deleteImage = new DeleteMessage();
      deleteImage.setChatId(chatId.toString());
      deleteImage.setMessageId(imageMessageId);
      try {
        execute(deleteImage);
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }

      messageIdKeeper.setImageMessageId(null);
    }

    telegramFacade.saveMessageIdKeeper(messageIdKeeper);
  }
}
