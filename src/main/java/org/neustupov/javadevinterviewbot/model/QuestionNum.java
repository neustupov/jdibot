package org.neustupov.javadevinterviewbot.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * Порядковый номер вопроса для авто-генерации последовательности в MongoDB
 */
@Data
public class QuestionNum {

  /**
   * Id
   */
  @Id
  private ObjectId id;

  /**
   * Сиквенс
   */
  public long seq;

  public QuestionNum(long seq) {
    this.seq = seq;
  }

}
