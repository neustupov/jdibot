package org.neustupov.javadevinterviewbot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserContext {

  @Id
  public Long userId;
  Level level;
  Category category;
  String searchField;
  RangePair range;
  String route;

  public UserContext(Long userId) {
    this.userId = userId;
  }
}
