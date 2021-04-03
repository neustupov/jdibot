package org.neustupov.javadevinterviewbot.botapi.buttons;

import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Buttons.*;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.utils.Emojis;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 * Класс формирует кнопки меню
 */
@Component
public class ButtonMaker {

  /**
   * Интерфейс, содержащий названия кнопок
   */
  public interface Buttons {

    String BACK = "Назад";
    String BACK_TO_START_MENU = "Вернуться в главное меню";
    String BACK_TO_CATEGORY = "Вернуться к категориям";
    String BACK_TO_LEVEL = "Вернуться к уровню";

    String OOP = "ООП";
    String COLLECTIONS = "Коллекции";
    String PATTERNS = "Паттерны";
    String SPRING = "Spring";
    String SPRING_PART_1 = "Spring part 1";
    String SPRING_PART_2 = "Spring part 2";
    String SPRING_PART_3 = "Spring part 3";

    String JUNIOR = "Junior";
    String MIDDLE = "Middle";
    String SENIOR = "Senior";

    String QUESTIONS = "Вопросы";
    String SEARCH = "Поиск";
    String TESTS = "Тестирование";

    String PREVIOUS = "Туда";
    String NEXT = "Сюда";
  }

  /**
   * Интерфейс, содержащий колбеки кнопок
   */
  public interface Callbacks {

    String BACK_BUTTON = "backButton";
    String NEW_SEARCH_BUTTON = "newSearchButton";
    String BACK_TO_START_MENU_BUTTON = "backToStartMenuButton";
    String BACK_TO_CATEGORY_BUTTON = "backToCategoryButton";
    String BACK_TO_LEVEL_BUTTON = "backToLevelButton";

    String OOP_CATEGORY_BUTTON = "buttonOOP";
    String COLLECTIONS_CATEGORY_BUTTON = "buttonCollections";
    String PATTERNS_CATEGORY_BUTTON = "buttonPatterns";
    String SPRING_BUTTON = "buttonSpring";
    String SPRING_PART_1_BUTTON = "buttonSpring_1";
    String SPRING_PART_2_BUTTON = "buttonSpring_2";
    String SPRING_PART_3_BUTTON = "buttonSpring_3";

    String JUNIOR_LEVEL_BUTTON = "buttonJunior";
    String MIDDLE_LEVEL_BUTTON = "buttonMiddle";
    String SENIOR_LEVEL_BUTTON = "buttonSenior";

    String QUESTIONS_BUTTON = "buttonQuestions";
    String SEARCH_BUTTON = "buttonSearch";
    String TESTS_BUTTON = "buttonTest";

    String PREVIOUS_BUTTON = "<-Button";
    String NEXT_BUTTON = "->Button";
  }

  /**
   * Формирует блок клавиатуры сообщения из мапы названий\кобеков кнопок и, в зависимости от
   * состояния, добавляет кнопки возврата к предыдущему состоянию
   *
   * @param buttonMap Мапа названий\кобеков кнопок
   * @param botState Текущее состояние
   * @return Блок клавиатуры
   */
  public InlineKeyboardMarkup getInlineMessageButtons(Map<String, String> buttonMap,
      BotState botState) {
    List<InlineKeyboardButton> menuButtons = new ArrayList<>();
    buttonMap.keySet()
        .forEach(key -> menuButtons
            .add(
                InlineKeyboardButton.builder().text(key).callbackData(buttonMap.get(key)).build()));
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    rows.add(menuButtons);
    if (botState.equals(BotState.SHOW_CATEGORY_MENU)) {
      rows.add(getBackToLevel());
    } else if (botState.equals(BotState.SHOW_LEVEL_MENU)) {
      rows.add(getBackToStart());
    }
    return InlineKeyboardMarkup.builder().keyboard(rows).build();
  }

  /**
   * Формирует блок клавиатуры для возврата в предыдущее состояние
   *
   * @return Блок клавиатуры
   */
  public InlineKeyboardMarkup getBackButton() {
    return InlineKeyboardMarkup.builder()
        .keyboard(Collections.singletonList(getSimpleBackButton()))
        .build();
  }

  /**
   * Формирует блок клавиатуры для возврата в главное меню бота
   *
   * @return Блок клавиатуры
   */
  public InlineKeyboardMarkup getBackToStartMenuButton() {
    return InlineKeyboardMarkup.builder()
        .keyboard(Collections.singletonList(getBackToStart()))
        .build();
  }

  /**
   * Формирует блок клавиатуры пагинации с кнопками возврата
   *
   * @return Блок клавиатуры
   */
  public InlineKeyboardMarkup getPaginationButton(boolean previous, boolean next, BotState state) {
    List<InlineKeyboardButton> paginationButton = new ArrayList<>();
    if (previous) {
      InlineKeyboardButton buttonPrevious = InlineKeyboardButton.builder()
          .text(Emojis.PREVIOUS + " " + PREVIOUS)
          .callbackData(PREVIOUS_BUTTON)
          .build();
      paginationButton.add(buttonPrevious);
    }
    if (next) {
      InlineKeyboardButton buttonNext = InlineKeyboardButton.builder()
          .text(NEXT + " " + Emojis.NEXT)
          .callbackData(NEXT_BUTTON)
          .build();
      paginationButton.add(buttonNext);
    }
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    rows.add(paginationButton);
    switch (state) {
      case FILLING_SEARCH:
        rows.add(getBackToStart());
        break;
      case SHOW_CATEGORY:
        rows.add(getBackToCategory());
        break;
    }
    return InlineKeyboardMarkup.builder().keyboard(rows).build();
  }

  /**
   * Формирует кнопки возврата к предыдущему состоянию
   *
   * @return Список кнопок
   */
  private List<InlineKeyboardButton> getSimpleBackButton() {
    return getInlineKeyboardButtons(Emojis.TOP + " " + BACK, BACK_BUTTON);
  }

  /**
   * Формирует кнопки возврата к выбору уровня
   *
   * @return Список кнопок
   */
  private List<InlineKeyboardButton> getBackToLevel() {
    return getInlineKeyboardButtons(Emojis.TOP + "   " + BACK_TO_LEVEL + "   " + Emojis.TOP,
        BACK_TO_LEVEL_BUTTON);
  }

  /**
   * Формирует кнопки возврата к главному меню
   *
   * @return Список кнопок
   */
  private List<InlineKeyboardButton> getBackToStart() {
    return getInlineKeyboardButtons(Emojis.TOP + "   " + BACK_TO_START_MENU + "   " + Emojis.TOP,
        BACK_TO_START_MENU_BUTTON);
  }

  /**
   * Формирует кнопки возврата к выбору категорий
   *
   * @return Список кнопок
   */
  private List<InlineKeyboardButton> getBackToCategory() {
    return getInlineKeyboardButtons(Emojis.TOP + "   " + BACK_TO_CATEGORY + "   " + Emojis.TOP,
        BACK_TO_CATEGORY_BUTTON);
  }

  /**
   * Формирует список кнопок
   *
   * @return Список кнопок
   */
  private List<InlineKeyboardButton> getInlineKeyboardButtons(String button,
      String callback) {
    InlineKeyboardButton backToStart = InlineKeyboardButton.builder()
        .text(button)
        .callbackData(callback)
        .build();
    return Collections.singletonList(backToStart);
  }
}
