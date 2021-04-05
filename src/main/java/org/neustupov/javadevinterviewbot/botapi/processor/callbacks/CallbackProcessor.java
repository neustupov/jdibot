package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Начинает цепочку по обработке колбеков
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallbackProcessor {

  /**
   * Колбек
   */
  Callback callback;

  public CallbackProcessor(StartMenuCallbacks callback) {
    this.callback = callback;
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

    return botResponse;
  }
}
