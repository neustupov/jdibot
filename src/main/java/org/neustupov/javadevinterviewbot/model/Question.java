package org.neustupov.javadevinterviewbot.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

  @NotNull
  @NotEmpty
  String link;
  @NotNull
  @NotEmpty
  Category category;
  @NotNull
  @NotEmpty
  Level level;
  @NotNull
  @NotEmpty
  String smallDescription;
  @NotNull
  @NotEmpty
  String largeDescription;
}
