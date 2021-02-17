package org.neustupov.javadevinterviewbot;

import java.io.File;
import java.io.FileNotFoundException;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.TelegramFacade;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
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

  public JavaDevInterviewBot(DefaultBotOptions options, TelegramFacade telegramFacade) {
    super(options);
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
      return telegramFacade.handleUpdate(update);
    }
    return null;
  }

  /**
   * Отправляет фото в чат - !! Должны быть указаны проперти бота - название, токен и хук !!
   *
   * @param chatId ID чата
   * @param imageCaption Текст под фото
   * @param imagePath Путь к файлу
   */
  public void sendPhoto(long chatId, String imageCaption, String imagePath) {
    File image = null;
    try {
      image = ResourceUtils.getFile("classpath:" + imagePath);
    } catch (FileNotFoundException e) {
      log.error("Не найден файл по пути: " + imagePath);
    }
    if (image != null) {
      SendPhoto sendPhoto = new SendPhoto().setPhoto(image);
      sendPhoto.setChatId(chatId);
      sendPhoto.setCaption(imageCaption);
      try {
        execute(sendPhoto);
      } catch (TelegramApiException e) {
        log.error("Не удалось отправить файл " + image.getName() + " " + e.getMessage());
      }
    }
  }
}
