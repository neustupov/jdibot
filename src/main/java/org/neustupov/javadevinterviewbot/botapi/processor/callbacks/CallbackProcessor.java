package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.model.MessageIdStorage;
import org.neustupov.javadevinterviewbot.service.MessageIdStorageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Начинает цепочку по обработке колбеков
 */
@Component
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public class CallbackProcessor {

  /**
   * Колбек
   */
  Callback callback;

  /**
   * Сервис хранилища предыдущих сообщений
   */
  MessageIdStorageService messageIdStorageService;

  public CallbackProcessor(
      StartMenuCallbacks callback,
      MessageIdStorageService messageIdStorageService) {
    this.callback = callback;
    this.messageIdStorageService = messageIdStorageService;
  }

  /**
   * Запускает обработку колбеков
   *
   * @param callbackQuery Колбек
   * @return BotApiMethod<?>
   * @throws UnsupportedOperationException Выбрасывается при отсутствии результата обработки
   * колбека
   */
  public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery)
      throws UnsupportedOperationException {

    final int userId = callbackQuery.getFrom().getId();
    final Message message = callbackQuery.getMessage();
    BotApiMethod<?> botResponse = callback.handleCallback(callbackQuery, userId, message);

    if (botResponse == null) {
      throw new UnsupportedOperationException("Callback handler not found");
    }

    setCallbackMessageIdsInKeeper(callbackQuery);

    return botResponse;
  }

  /**
   * Фиксирует Id предыдущих сообщений, колбеков и необходимость их удалять
   * @param callbackQuery Колбек
   */
  private void setCallbackMessageIdsInKeeper(CallbackQuery callbackQuery) {
    Message message = callbackQuery.getMessage();
    MessageIdStorage messageIdStorage = messageIdStorageService
        .getStorageByChatId(message.getChatId());
    messageIdStorage.setNeedDeletePreviousPrevious(false);
    messageIdStorage.setPreviousPreviousMessageId(message.getMessageId());
    messageIdStorage.setNeedDeleteImage(true);
    messageIdStorageService.save(messageIdStorage);
  }
}
