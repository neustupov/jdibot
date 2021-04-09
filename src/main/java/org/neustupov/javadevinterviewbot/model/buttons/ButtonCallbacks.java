package org.neustupov.javadevinterviewbot.model.buttons;

import lombok.AllArgsConstructor;

/**
 * Колбеки кнопок
 */
@AllArgsConstructor
public enum ButtonCallbacks {

  BACK_BUTTON("backButton"),
  NEW_SEARCH_BUTTON("newSearchButton"),
  BACK_TO_START_MENU_BUTTON("backToStartMenuButton"),
  BACK_TO_CATEGORY_BUTTON("backToCategoryButton"),
  BACK_TO_LEVEL_BUTTON("backToLevelButton"),

  OOP_CATEGORY_BUTTON("buttonOOP"),
  COLLECTIONS_CATEGORY_BUTTON("buttonCollections"),
  PATTERNS_CATEGORY_BUTTON("buttonPatterns"),
  SPRING_BUTTON("buttonSpring"),
  SPRING_PART_1_BUTTON("buttonSpring_1"),
  SPRING_PART_2_BUTTON("buttonSpring_2"),
  SPRING_PART_3_BUTTON("buttonSpring_3"),

  JUNIOR_LEVEL_BUTTON("buttonJunior"),
  MIDDLE_LEVEL_BUTTON("buttonMiddle"),
  SENIOR_LEVEL_BUTTON("buttonSenior"),

  QUESTIONS_BUTTON("buttonQuestions"),
  SEARCH_BUTTON("buttonSearch"),
  TESTS_BUTTON("buttonTest"),

  PREVIOUS_BUTTON("<-Button"),
  NEXT_BUTTON("->Button");

  /**
   * Название колбека
   */
  private String name;

  /**
   * Ищет колбек по названию
   *
   * @param name Название
   * @return Колбек
   */
  public static ButtonCallbacks valueOfName(String name) {
    for (ButtonCallbacks e : values()) {
      if (e.name.equals(name)) {
        return e;
      }
    }
    return null;
  }

  /**
   * Возвращает название колбека
   *
   * @return Название колбека
   */
  @Override
  public String toString() {
    return name;
  }
}
