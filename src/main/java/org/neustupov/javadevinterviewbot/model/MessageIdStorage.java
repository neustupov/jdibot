package org.neustupov.javadevinterviewbot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

/**
 * Класс, содержащий данные о ранее полученных сообщениях и необходимости их удаления
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageIdStorage {

  /**
   * ID чата
   */
  @Id
  Long chatId;

  /**
   * Необходимость удаления предыдущего сообщения
   */
  boolean needDeletePrevious;

  /**
   * Предыдущее сообщение
   */
  Integer previousMessageId;

  /**
   * Необходимость удаления пред-предыдущего сообщения
   */
  boolean needDeletePreviousPrevious;

  /**
   * Пред-предыдущее сообщение
   */
  Integer previousPreviousMessageId;

  /**
   * Необходимость удаления сообщения с изображением
   */
  boolean needDeleteImage;

  /**
   * Сообщение с изображением
   */
  Integer imageMessageId;
}
