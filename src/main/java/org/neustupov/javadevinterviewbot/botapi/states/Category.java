package org.neustupov.javadevinterviewbot.botapi.states;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Category {

  OOP("Категория - ООП"),
  COLLECTIONS("Категория - Коллекции"),
  PATTERNS("Категория - Паттерны"),
  SPRING("Категория - Spring");

  private String name;

  @Override
  public String toString() {
    return name;
  }
}
