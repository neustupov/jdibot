package org.neustupov.javadevinterviewbot.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {

  Category category;
  Level level;
  String link;
  String smallDescription;
  String largeDescription;
}
