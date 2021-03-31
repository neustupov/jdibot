package org.neustupov.javadevinterviewbot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageIdKeeper {

  @Id
  Long chatId;

  Integer previousMessageId;
  Integer currentMessageId;
}
