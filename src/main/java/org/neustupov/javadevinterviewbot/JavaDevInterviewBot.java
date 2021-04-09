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

/**
 * Бин бота
 */
@Slf4j
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JavaDevInterviewBot extends TelegramWebhookBot {

  /**
   * Веб-хук
   */
  String webHookPath;

  /**
   * Название бота
   */
  String botUserName;

  /**
   * Токен бота
   */
  String botToken;

  /**
   * Фасад бота
   */
  TelegramFacade telegramFacade;

  public JavaDevInterviewBot(TelegramFacade telegramFacade) {
    this.telegramFacade = telegramFacade;
  }

  /**
   * Обрабатывает поступающий апдейты
   *
   * @param update Update
   * @return Ответ приложения
   */
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

  /**
   * Обновляет сообщение
   *
   * @param botResponseData Данные ответа
   */
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

  /**
   * Удаляет предыдущие сообщения
   *
   * @param messageIdStorage Хранилище предыдущих сообщений
   */
  private void deletePreviousMessages(MessageIdStorage messageIdStorage) {
    String chatId = messageIdStorage.getChatId().toString();

    Integer previousMessageId = messageIdStorage.getPreviousMessageId();
    if (previousMessageId != null && messageIdStorage.isNeedDeletePrevious()) {
      try {
        execute(getDeleteMessage(chatId, previousMessageId));
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }

      log.info("Delete message with ID:{}", previousMessageId);
      messageIdStorage.setNeedDeletePrevious(false);
      messageIdStorage.setPreviousMessageId(null);
    }

    Integer previousPreviousMessageId = messageIdStorage.getPreviousPreviousMessageId();
    if (previousPreviousMessageId != null && messageIdStorage.isNeedDeletePreviousPrevious()) {
      try {
        execute(getDeleteMessage(chatId, previousPreviousMessageId));
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }

      log.info("Delete message with ID:{}", previousPreviousMessageId);
      messageIdStorage.setNeedDeletePreviousPrevious(false);
      messageIdStorage.setPreviousPreviousMessageId(null);
    }

    deleteImage(messageIdStorage);
  }

  /**
   * Удаляет сообщение с изображением
   *
   * @param messageIdStorage Хранилище предыдущих сообщений
   */
  private void deleteImage(MessageIdStorage messageIdStorage) {
    String chatId = messageIdStorage.getChatId().toString();
    Integer imageMessageId = messageIdStorage.getImageMessageId();
    if (imageMessageId != null && messageIdStorage.isNeedDeleteImage()) {
      try {
        execute(getDeleteMessage(chatId, imageMessageId));
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }

      log.info("Delete message with Image and ID:{}", imageMessageId);
      messageIdStorage.setImageMessageId(null);
    }

    telegramFacade.saveMessageIdKeeper(messageIdStorage);
  }

  /**
   * Создает объект для удаления сообщения
   *
   * @param chatId chatId
   * @param messageId messageId
   * @return DeleteMessage
   */
  private DeleteMessage getDeleteMessage(String chatId, Integer messageId) {
    return DeleteMessage.builder()
        .chatId(chatId)
        .messageId(messageId)
        .build();
  }

  /**
   * Возвращает название бота
   *
   * @return Название бота
   */
  @Override
  public String getBotUsername() {
    return botUserName;
  }

  /**
   * Возвращает токен бота
   *
   * @return Токен бота
   */
  @Override
  public String getBotToken() {
    return botToken;
  }

  /**
   * Возвращает веб-хук
   *
   * @return Веб-хук
   */
  @Override
  public String getBotPath() {
    return webHookPath;
  }
}
