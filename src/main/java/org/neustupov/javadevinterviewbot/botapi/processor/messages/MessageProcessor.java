package org.neustupov.javadevinterviewbot.botapi.processor.messages;

import static org.neustupov.javadevinterviewbot.botapi.processor.messages.MessageProcessor.Commands.QUESTION;
import static org.neustupov.javadevinterviewbot.botapi.processor.messages.MessageProcessor.Commands.START;

import java.io.File;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.neustupov.javadevinterviewbot.JavaDevInterviewBot;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.neustupov.javadevinterviewbot.model.MessageIdStorage;
import org.neustupov.javadevinterviewbot.service.MessageIdStorageService;
import org.neustupov.javadevinterviewbot.utils.ImageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обрабатывает поступившее текстовое сообщение
 */
@Slf4j
@Component
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public class MessageProcessor {

  /**
   * Бин бота
   */
  JavaDevInterviewBot bot;

  /**
   * Кеш данных пользователя
   */
  DataCache dataCache;

  /**
   * Контекст бота
   */
  BotStateContext botStateContext;

  /**
   * Сервис хранилища предыдущих сообщений
   */
  MessageIdStorageService messageIdStorageService;

  /**
   * Утилита для работы с изображениями
   */
  ImageUtil imageUtil;

  /**
   * Комманды, принимаемые ботом
   */
  interface Commands {

    String START = "/start";
    String QUESTION = "/q";
  }

  public MessageProcessor(JavaDevInterviewBot bot,
      DataCache dataCache, BotStateContext botStateContext,
      MessageIdStorageService messageIdStorageService,
      ImageUtil imageUtil) {
    this.bot = bot;
    this.dataCache = dataCache;
    this.botStateContext = botStateContext;
    this.messageIdStorageService = messageIdStorageService;
    this.imageUtil = imageUtil;
  }

  /**
   * Обрабатывает поступившее текстовое сообщение
   *
   * @param message Сообщение
   * @return Ответ
   */
  public SendMessage handleInputMessage(Message message) {
    MessageIdStorage messageIdStorage = messageIdStorageService
        .getStorageByChatId(message.getChatId());
    messageIdStorage.setPreviousMessageId(message.getMessageId());

    String inputMsg = message.getText();
    BotState botState;

    if (inputMsg.equalsIgnoreCase(START)) {
      botState = BotState.SHOW_START_MENU;
      processStartMessage(message);
    } else {
      botState = dataCache.getUserCurrentBotState(message.getFrom().getId());
    }

    SendMessage replyMessage;

    if (inputMsg.startsWith(QUESTION)) {
      replyMessage =  processQuestionMessage(message, messageIdStorage);
    } else {
      replyMessage = botStateContext.processInputMessage(botState, message);
    }

    if (replyMessage != null) {
      setTextMessageIdsInKeeper(messageIdStorage);
    }

    return replyMessage;
  }

  /**
   * Обрабатывает сообщение с коммандой "/start"
   *
   * @param message Сообщение
   */
  private void processStartMessage(Message message) {
    int userId = message.getFrom().getId();
    dataCache.cleanAll(userId);
    dataCache.setUserCurrentBotState(userId, BotState.SHOW_START_MENU);
    botStateContext.processInputMessage(BotState.SHOW_START_MENU, message);
  }

  /**
   * Обрабатывает сообщение с коммандой "/q"
   *
   * @param message Сообщение
   */
  private SendMessage processQuestionMessage(Message message, MessageIdStorage messageIdStorage) {
    dataCache.setUserCurrentBotState(message.getFrom().getId(), BotState.SHOW_QUESTION);
    Message responseImageMessage = processImage(message.getText(), message.getChatId());

    setImageMessageIdsInKeeper(messageIdStorage, responseImageMessage);

    return botStateContext.processInputMessage(BotState.SHOW_QUESTION, message);
  }

  /**
   * Фиксирует Id сообщения с изображением и необходимость его удалить
   */
  private void setImageMessageIdsInKeeper(MessageIdStorage messageIdStorage,
      Message responseImageMessage) {
    if (messageIdStorage != null && responseImageMessage != null) {
      messageIdStorage.setImageMessageId(responseImageMessage.getMessageId());
      messageIdStorage.setNeedDeleteImage(false);
      messageIdStorageService.save(messageIdStorage);
    }
  }

  /**
   * Фиксирует Id текстового сообщения или команды и необходимость его удалить
   */
  private void setTextMessageIdsInKeeper(MessageIdStorage messageIdStorage) {
    if (messageIdStorage.getPreviousMessageId() != null
        && messageIdStorage.getPreviousPreviousMessageId() == null) {
      messageIdStorage.setPreviousPreviousMessageId(messageIdStorage.getPreviousMessageId() - 1);
    }
    messageIdStorage.setNeedDeletePrevious(true);
    messageIdStorage.setNeedDeletePreviousPrevious(true);
    messageIdStorageService.save(messageIdStorage);
  }

  /**
   * Работает с изображением
   *
   * @param inputMsg Сообщение команды
   * @param chatId chatId
   * @return Результат отправки изображения
   */
  private Message processImage(String inputMsg, long chatId) {
    Optional<Binary> imageData = imageUtil.getImageData(inputMsg);
    Message message = null;
    if (imageData.isPresent()) {
      File imageTempFile = imageUtil.getImageFile(imageData.get());
      message = sendPhoto(chatId, imageTempFile);
      imageUtil.deleteTempFile(imageTempFile);
    }
    return message;
  }

  /**
   * Отправляет сообщение
   *
   * @param chatId chatId
   * @param file Изображение
   * @return Результат отправки изображения
   */
  private Message sendPhoto(Long chatId, File file) {
    Message message = null;
    if (file != null) {
      SendPhoto sendPhoto = SendPhoto.builder()
          .photo(new InputFile(file))
          .chatId(chatId.toString())
          .caption("")
          .build();
      try {
        message = bot.execute(sendPhoto);
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }
    }
    return message;
  }
}
