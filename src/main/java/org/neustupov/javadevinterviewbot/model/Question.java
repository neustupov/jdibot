package org.neustupov.javadevinterviewbot.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.states.Category;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {

  Category category;
  String link;
  String smallDescription;
  String largeDescription;
}
