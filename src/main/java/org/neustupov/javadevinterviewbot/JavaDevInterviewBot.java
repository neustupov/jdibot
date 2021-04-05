package org.neustupov.javadevinterviewbot;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.TelegramFacade;
import org.neustupov.javadevinterviewbot.model.BotResponseData;
import org.neustupov.javadevinterviewbot.model.MessageIdStorage;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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
      BotApiMethod<?> method = botResponseData.getBotApiMethod();

      if (method instanceof AnswerCallbackQuery) {
        return method;
      }

      MessageIdStorage messageIdStorage = botResponseData.getMessageIdStorage();
      if (update.hasCallbackQuery()) {
        User user = update.getCallbackQuery().getMessage().getFrom();
        if (user != null && user.getIsBot()) {
          updateMessage(botResponseData);
        }
        deleteImage(messageIdStorage);
      } else {
        deletePreviousMessages(messageIdStorage);
        return method;
      }
    }
    return null;
  }

  private void updateMessage(BotResponseData botResponseData) {
    SendMessage sendMessage = (SendMessage) botResponseData.getBotApiMethod();
    if (sendMessage != null) {
      EditMessageText editMessageText = EditMessageText.builder()
          .chatId(sendMessage.getChatId())
          .messageId(botResponseData.getMessageId())
          .text(sendMessage.getText())
          .replyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup())
          .build();
      try {
        execute(editMessageText);
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }
      log.info("Message with ID:{} updated", botResponseData.getMessageId());
    }
  }

  private void deletePreviousMessages(MessageIdStorage messageIdStorage) {
    Long chatId = messageIdStorage.getChatId();

    Integer previousMessageId = messageIdStorage.getPreviousMessageId();
    if (previousMessageId != null && messageIdStorage.isNeedDeletePrevious()) {

      DeleteMessage deleteMessage = new DeleteMessage();
      deleteMessage.setChatId(chatId.toString());
      deleteMessage.setMessageId(previousMessageId);
      try {
        execute(deleteMessage);
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }

      log.info("Delete message with ID:{}", previousMessageId);
      messageIdStorage.setNeedDeletePrevious(false);
      messageIdStorage.setPreviousMessageId(null);
    }

    Integer previousPreviousMessageId = messageIdStorage.getPreviousPreviousMessageId();
    if (previousPreviousMessageId != null && messageIdStorage.isNeedDeletePreviousPrevious()) {

      DeleteMessage deleteMessage = new DeleteMessage();
      deleteMessage.setChatId(chatId.toString());
      deleteMessage.setMessageId(previousPreviousMessageId);
      try {
        execute(deleteMessage);
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }

      log.info("Delete message with ID:{}", previousPreviousMessageId);
      messageIdStorage.setNeedDeletePreviousPrevious(false);
      messageIdStorage.setPreviousPreviousMessageId(null);
    }

    deleteImage(messageIdStorage);
  }

  private void deleteImage(MessageIdStorage messageIdStorage) {
    Long chatId = messageIdStorage.getChatId();
    Integer imageMessageId = messageIdStorage.getImageMessageId();
    if (imageMessageId != null && messageIdStorage.isNeedDeleteImage()) {
      DeleteMessage deleteImage = new DeleteMessage();
      deleteImage.setChatId(chatId.toString());
      deleteImage.setMessageId(imageMessageId);
      try {
        execute(deleteImage);
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }

      log.info("Delete message with Image and ID:{}", imageMessageId);
      messageIdStorage.setImageMessageId(null);
    }

    telegramFacade.saveMessageIdKeeper(messageIdStorage);
  }
}
