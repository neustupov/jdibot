package org.neustupov.javadevinterviewbot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.model.menu.Category;
import org.neustupov.javadevinterviewbot.model.menu.Level;
import org.springframework.data.annotation.Id;

/**
 * Контекст, содержащий данные для конкретного пользователя
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserContext {

  /**
   * Id пользователя
   */
  @Id
  public Long userId;

  /**
   * Уровень
   */
  Level level;

  /**
   * Категория
   */
  Category category;

  /**
   * Текст поиска
   */
  String searchField;

  /**
   * Диапазон пагинации
   */
  RangePair range;

  /**
   * Направление пагинации
   */
  String route;

  public UserContext(Long userId) {
    this.userId = userId;
  }
}
