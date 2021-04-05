package org.neustupov.javadevinterviewbot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.Binary;
import org.neustupov.javadevinterviewbot.model.menu.Category;
import org.neustupov.javadevinterviewbot.model.menu.Level;
import org.springframework.data.annotation.Id;

/**
 * Бин вопроса
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {

  /**
   * Id
   */
  @Id
  long id;

  /**
   * Изображение
   */
  @JsonIgnore
  Binary image;

  /**
   * Категория
   */
  @NotNull
  Category category;

  /**
   * Уровень
   */
  @NotNull
  Level level;

  /**
   * Описание
   */
  @NotNull
  @NotEmpty
  String smallDescription;

  /**
   * Основное описание
   */
  @NotNull
  @NotEmpty
  String largeDescription;
}
