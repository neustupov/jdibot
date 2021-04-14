package org.neustupov.javadevinterviewbot.model.menu;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Категории
 */
@AllArgsConstructor
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public enum Category {

  OOP("Категория - ООП"),
  COLLECTIONS("Категория - Коллекции"),
  PATTERNS("Категория - Паттерны"),
  SPRING("Категория - Spring");

  /**
   * Название категории
   */
  String name;

  /**
   * Возвращает название категории
   *
   * @return Название
   */
  @Override
  public String toString() {
    return name;
  }
}
