package org.neustupov.javadevinterviewbot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.Binary;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {

  @Id
  long id;
  @JsonIgnore
  Binary image;
  @NotNull
  Category category;
  @NotNull
  Level level;
  @NotNull
  @NotEmpty
  String smallDescription;
  @NotNull
  @NotEmpty
  String largeDescription;
}
