package org.neustupov.javadevinterviewbot.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
public class QuestionNum {

  @Id
  private ObjectId id;

  public long seq;

  public QuestionNum(long seq) {
    this.seq = seq;
  }

}
