package org.neustupov.javadevinterviewbot;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

import java.io.File;
import java.io.FileNotFoundException;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.TelegramFacade;
import org.neustupov.javadevinterviewbot.model.BotResponseData;
import org.neustupov.javadevinterviewbot.model.MessageIdKeeper;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
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
      Integer previousMessageId = messageIdKeeper.getPreviousMessageId();
      if (previousMessageId != null){
        deletePreviousMessage(messageIdKeeper.getChatId(), previousMessageId);
      }
      return botResponseData.getBotApiMethod();
    }
    return null;
  }

  public void sendPhoto(Long chatId, String imageCaption, File file) {
    sendPhotoWithCaption(chatId, imageCaption, file);
  }

  /**
   * Отправляет фото в чат - !! Должны быть указаны проперти бота - название, токен и хук !!
   *
   * @param chatId ID чата
   * @param imageCaption Текст под фото
   * @param imagePath Путь к файлу
   */
  public void sendPhoto(Long chatId, String imageCaption, String imagePath) {
    File image = null;
    try {
      image = ResourceUtils.getFile(CLASSPATH_URL_PREFIX + imagePath);
    } catch (FileNotFoundException e) {
      log.error("Не найден файл по пути: " + imagePath, e);
    }
    sendPhotoWithCaption(chatId, imageCaption, image);
  }

  private void sendPhotoWithCaption(Long chatId, String imageCaption, File file) {
    if (file != null) {
      SendPhoto sendPhoto = SendPhoto.builder()
          .photo(new InputFile(file))
          .chatId(chatId.toString())
          .caption(imageCaption).build();
      try {
        execute(sendPhoto);
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  private void deletePreviousMessage(Long chatId, Integer previousMessageId) {
    DeleteMessage deleteMessage = new DeleteMessage();
    deleteMessage.setChatId(chatId.toString());
    deleteMessage.setMessageId(previousMessageId);
    try {
      execute(deleteMessage);
    } catch (TelegramApiException e) {
      log.error(e.getMessage(), e);
    }
  }
}
