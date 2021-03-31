package org.neustupov.javadevinterviewbot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotResponseData {

  BotApiMethod<?> botApiMethod;
  MessageIdKeeper messageIdKeeper;
}
