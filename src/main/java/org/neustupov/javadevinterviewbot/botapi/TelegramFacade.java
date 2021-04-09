package org.neustupov.javadevinterviewbot.botapi;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.processor.callbacks.CallbackProcessor;
import org.neustupov.javadevinterviewbot.botapi.processor.messages.MessageProcessor;
import org.neustupov.javadevinterviewbot.model.BotResponseData;
import org.neustupov.javadevinterviewbot.model.MessageIdStorage;
import org.neustupov.javadevinterviewbot.service.MessageIdStorageService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Фасад бота
 */
@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramFacade {

  /**
   * Обработчик колбеков
   */
  CallbackProcessor callbackProcessor;

  /**
   * Обработчик текстовых сообщений
   */
  MessageProcessor messageProcessor;

  /**
   * Сервис предыдущих сообщений
   */
  MessageIdStorageService messageIdStorageService;

  public TelegramFacade(
      CallbackProcessor callbackProcessor,
      @Lazy MessageProcessor messageProcessor,
      MessageIdStorageService messageIdStorageService) {
    this.callbackProcessor = callbackProcessor;
    this.messageProcessor = messageProcessor;
    this.messageIdStorageService = messageIdStorageService;
  }

  /**
   * Обрабатывает поступивший апдейт
   *
   * @param update Update
   * @return Объект с ответной информацией
   */
  public BotResponseData handleUpdate(Update update) {
    BotResponseData botResponseData = null;

    if (update.hasCallbackQuery()) {
      CallbackQuery callbackQuery = update.getCallbackQuery();

      BotApiMethod<?> botResponse = null;
      try {
        botResponse = callbackProcessor.processCallbackQuery(callbackQuery);
      } catch (UnsupportedOperationException e) {
        log.error(e.getMessage(), e);
      }

      botResponseData = BotResponseData.builder()
          .messageId(callbackQuery.getMessage().getMessageId())
          .botApiMethod(botResponse)
          .messageIdStorage(
              messageIdStorageService.getStorageByChatId(callbackQuery.getMessage().getChatId()))
          .build();

      log.info("New CallbackQuery from User:{}, userId:{}, with messageId:{} and data:{}",
          callbackQuery.getFrom().getFirstName() + " " + callbackQuery.getFrom().getLastName(),
          callbackQuery.getFrom().getId(), callbackQuery.getMessage().getMessageId(),
          callbackQuery.getData());
    }

    Message message = update.getMessage();
    if (message != null && message.hasText()) {
      SendMessage replyMessage = messageProcessor.handleInputMessage(message);

      botResponseData = BotResponseData.builder()
          .messageId(message.getMessageId())
          .botApiMethod(replyMessage)
          .messageIdStorage(messageIdStorageService.getStorageByChatId(message.getChatId()))
          .build();

      log.info("New message from User:{}, chatId:{}, messageId:{} with text: {}",
          message.getFrom().getFirstName() + " " + message.getFrom().getLastName(),
          message.getChatId(),
          message.getMessageId(),
          message.getText());
    }

    return botResponseData;
  }

  /**
   * Сохраняет объект, содержащий информацию о предыдущих сообщениях
   *
   * @param messageIdStorage Объект, содержащий информацию о предыдущих сообщениях
   */
  public void saveMessageIdKeeper(MessageIdStorage messageIdStorage) {
    messageIdStorageService.save(messageIdStorage);
  }
}
