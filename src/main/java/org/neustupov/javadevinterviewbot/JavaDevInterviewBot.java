package org.neustupov.javadevinterviewbot;

import java.io.File;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.TelegramFacade;
import org.neustupov.javadevinterviewbot.model.BotResponseData;
import org.neustupov.javadevinterviewbot.model.MessageIdKeeper;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
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
      deletePreviousMessage(messageIdKeeper);
      return botResponseData.getBotApiMethod();
    }
    return null;
  }

  public Message sendPhoto(Long chatId, String imageCaption, File file) {
    return sendPhotoWithCaption(chatId, imageCaption, file);
  }

  private Message sendPhotoWithCaption(Long chatId, String imageCaption, File file) {
    Message message = null;
    if (file != null) {
      SendPhoto sendPhoto = SendPhoto.builder()
          .photo(new InputFile(file))
          .chatId(chatId.toString())
          .caption(imageCaption).build();
      try {
        message = execute(sendPhoto);
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }
    }
    return message;
  }

  private void deletePreviousMessage(MessageIdKeeper messageIdKeeper) {
    Integer previousMessageId = messageIdKeeper.getPreviousMessageId();
    Integer imageMessageId = messageIdKeeper.getImageMessageId();

    if (previousMessageId != null && messageIdKeeper.isNeedDelete()) {
      Long chatId = messageIdKeeper.getChatId();

      DeleteMessage deleteMessage = new DeleteMessage();
      deleteMessage.setChatId(chatId.toString());
      deleteMessage.setMessageId(previousMessageId);
      try {
        execute(deleteMessage);
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }

      if (imageMessageId != null) {
        DeleteMessage deleteImage = new DeleteMessage();
        deleteImage.setChatId(chatId.toString());
        deleteImage.setMessageId(imageMessageId);
        try {
          execute(deleteImage);
        } catch (TelegramApiException e) {
          log.error(e.getMessage(), e);
        }
      }
    }
  }
}
