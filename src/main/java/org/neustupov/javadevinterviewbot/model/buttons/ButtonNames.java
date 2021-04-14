package org.neustupov.javadevinterviewbot.model.buttons;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Названия кнопок
 */
@AllArgsConstructor
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public enum ButtonNames {

  BACK("Назад"),
  BACK_TO_START_MENU("Вернуться в главное меню"),
  BACK_TO_CATEGORY("Вернуться к категориям"),
  BACK_TO_LEVEL("Вернуться к уровню"),

  OOP("ООП"),
  COLLECTIONS("Коллекции"),
  PATTERNS("Паттерны"),
  SPRING("Spring"),
  SPRING_PART_1("Spring part 1"),
  SPRING_PART_2("Spring part 2"),
  SPRING_PART_3("Spring part 3"),

  JUNIOR("Junior"),
  MIDDLE("Middle"),
  SENIOR("Senior"),

  QUESTIONS("Вопросы"),
  SEARCH("Поиск"),
  TESTS("Тестирование"),

  PREVIOUS("Туда"),
  NEXT("Сюда");

  /**
   * Название
   */
  String name;

  /**
   * Возвращает название
   *
   * @return название
   */
  @Override
  public String toString() {
    return name;
  }
}
