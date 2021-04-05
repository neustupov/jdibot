package org.neustupov.javadevinterviewbot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageIdStorage {

  @Id
  Long chatId;

  boolean needDeletePrevious;
  Integer previousMessageId;

  boolean needDeletePreviousPrevious;
  Integer previousPreviousMessageId;

  boolean needDeleteImage;
  Integer imageMessageId;
}
