package org.neustupov.javadevinterviewbot;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.TelegramFacade;
import org.neustupov.javadevinterviewbot.model.BotResponseData;
import org.neustupov.javadevinterviewbot.model.MessageIdKeeper;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
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
      MessageIdKeeper messageIdKeeper = botResponseData.getMessageIdKeeper();

      if (method instanceof AnswerCallbackQuery) {
        return method;
      } else if (update.hasCallbackQuery()) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Message message = callbackQuery.getMessage();
        User user = message.getFrom();
        if (user != null && user.getIsBot()) {
          updateMessage(botResponseData);
        }
        deleteImage(messageIdKeeper);
      } else {
        deletePreviousMessages(messageIdKeeper);
        return botResponseData.getBotApiMethod();
      }
    }
    return null;
  }

  private void updateMessage(BotResponseData botResponseData) {
    BotApiMethod<?> method = botResponseData.getBotApiMethod();
    SendMessage sendMessage = (SendMessage) method;
    EditMessageText editMessageText = null;
    if (sendMessage != null) {
      editMessageText = EditMessageText.builder()
          .chatId(sendMessage.getChatId())
          .messageId(botResponseData.getMessageId())
          .text(sendMessage.getText())
          .replyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup())
          .build();
    }
    if (editMessageText != null) {
      try {
        execute(editMessageText);
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }
      log.info("Message with ID:{} updated", botResponseData.getMessageId());
    }
  }

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

      log.info("Delete message with ID:{}", previousMessageId);
      messageIdKeeper.setNeedDeletePrevious(false);
      messageIdKeeper.setPreviousMessageId(null);
    }

    Integer previousPreviousMessageId = messageIdKeeper.getPreviousPreviousMessageId();
    if (previousPreviousMessageId != null && messageIdKeeper.isNeedDeletePreviousPrevious()) {

      DeleteMessage deleteMessage = new DeleteMessage();
      deleteMessage.setChatId(chatId.toString());
      deleteMessage.setMessageId(previousPreviousMessageId);
      try {
        execute(deleteMessage);
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }

      log.info("Delete message with ID:{}", previousPreviousMessageId);
      messageIdKeeper.setNeedDeletePreviousPrevious(false);
      messageIdKeeper.setPreviousPreviousMessageId(null);
    }

    deleteImage(messageIdKeeper);
  }

  private void deleteImage(MessageIdKeeper messageIdKeeper) {
    Long chatId = messageIdKeeper.getChatId();
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

      log.info("Delete message with Image and ID:{}", imageMessageId);
      messageIdKeeper.setImageMessageId(null);
    }

    telegramFacade.saveMessageIdKeeper(messageIdKeeper);
  }
}
