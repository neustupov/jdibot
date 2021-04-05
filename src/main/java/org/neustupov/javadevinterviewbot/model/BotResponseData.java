package org.neustupov.javadevinterviewbot.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotResponseData {

  Integer messageId;
  BotApiMethod<?> botApiMethod;
  MessageIdStorage messageIdStorage;
}
