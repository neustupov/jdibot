package org.neustupov.javadevinterviewbot.model.menu;

import lombok.AllArgsConstructor;

/**
 * Категории
 */
@AllArgsConstructor
public enum Category {

  OOP("Категория - ООП"),
  COLLECTIONS("Категория - Коллекции"),
  PATTERNS("Категория - Паттерны"),
  SPRING("Категория - Spring");

  /**
   * Название категории
   */
  private String name;

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
