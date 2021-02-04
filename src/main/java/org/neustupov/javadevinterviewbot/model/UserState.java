package org.neustupov.javadevinterviewbot.model;

import lombok.Data;
import org.neustupov.javadevinterviewbot.state.State;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "userstates")
public class UserState {

  @Id
  private long chatId;
  private State state;
}
