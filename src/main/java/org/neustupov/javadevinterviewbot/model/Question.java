package org.neustupov.javadevinterviewbot.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.bson.types.Binary;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.springframework.data.annotation.Id;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {

  @Id
  long id;
  Binary image;
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
