package org.neustupov.javadevinterviewbot.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

/**
 * Объект с данными ответа
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotResponseData {

  /**
   * Id сообщения
   */
  Integer messageId;

  /**
   * Объект ответа приложения
   */
  BotApiMethod<?> botApiMethod;

  /**
   * Объект, содержащий данные о предыдущих сообщениях
   */
  MessageIdStorage messageIdStorage;
}
