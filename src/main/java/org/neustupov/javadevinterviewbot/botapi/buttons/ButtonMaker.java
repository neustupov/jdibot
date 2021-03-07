package org.neustupov.javadevinterviewbot.botapi.buttons;

import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Buttons.*;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.*;

import java.util.ArrayList;
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

    String PREVIOUS = "<-";
    String NEXT = "->";
  }

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

  public InlineKeyboardMarkup getInlineMessageButtons(Map<String, String> buttonMap,
      BotState botState) {
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<InlineKeyboardButton> menuButtons = new ArrayList<>();
    buttonMap.keySet()
        .forEach(key -> menuButtons
            .add(InlineKeyboardButton.builder().text(key).callbackData(buttonMap.get(key)).build()));
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    rows.add(menuButtons);
    if (botState.equals(BotState.SHOW_CATEGORY_MENU)) {
      rows.add(getBackToLevel());
    } else if (botState.equals(BotState.SHOW_LEVEL_MENU)) {
      rows.add(getBackToStart());
    }
    inlineKeyboardMarkup.setKeyboard(rows);
    return inlineKeyboardMarkup;
  }

  public InlineKeyboardMarkup getBackToQuestionsButton() {
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    rows.add(getSimpleBackButton());
    inlineKeyboardMarkup.setKeyboard(rows);
    return inlineKeyboardMarkup;
  }

  public InlineKeyboardMarkup getBackToStartMenuButton() {
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    rows.add(getBackToStart());
    inlineKeyboardMarkup.setKeyboard(rows);

    return inlineKeyboardMarkup;
  }

  private List<InlineKeyboardButton> getSimpleBackButton() {
    return getInlineKeyboardButtons(Emojis.BACK + " " + BACK, BACK_BUTTON);
  }

  private List<InlineKeyboardButton> getBackToLevel() {
    return getInlineKeyboardButtons(Emojis.BACK + "   " + BACK_TO_LEVEL + "   " + Emojis.BACK,
        BACK_TO_LEVEL_BUTTON);
  }

  private List<InlineKeyboardButton> getBackToStart() {
    return getInlineKeyboardButtons(Emojis.TOP + "   " + BACK_TO_START_MENU + "   " + Emojis.TOP,
        BACK_TO_START_MENU_BUTTON);
  }

  private List<InlineKeyboardButton> getBackToCategory() {
    return getInlineKeyboardButtons(BACK_TO_CATEGORY, BACK_TO_CATEGORY_BUTTON);
  }

  private List<InlineKeyboardButton> getInlineKeyboardButtons(String button,
      String callback) {
    InlineKeyboardButton backToStart = InlineKeyboardButton.builder().text(button).build();
    backToStart.setCallbackData(callback);
    List<InlineKeyboardButton> backToStartButton = new ArrayList<>();
    backToStartButton.add(backToStart);
    return backToStartButton;
  }

  public InlineKeyboardMarkup getPaginationButton(boolean previous, boolean next, BotState state) {
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();

    List<InlineKeyboardButton> paginationButton = new ArrayList<>();
    if (previous) {
      InlineKeyboardButton buttonPrevious = InlineKeyboardButton.builder().text(PREVIOUS).build();
      buttonPrevious.setCallbackData(PREVIOUS_BUTTON);
      paginationButton.add(buttonPrevious);
    }
    if (next) {
      InlineKeyboardButton buttonNext = InlineKeyboardButton.builder().text(NEXT).build();
      buttonNext.setCallbackData(NEXT_BUTTON);
      paginationButton.add(buttonNext);
    }
    rows.add(paginationButton);
    if (state.equals(BotState.FILLING_SEARCH)) {
      rows.add(getBackToStart());
    } else if (state.equals(BotState.SHOW_CATEGORY)) {
      rows.add(getBackToCategory());
    }
    inlineKeyboardMarkup.setKeyboard(rows);
    return inlineKeyboardMarkup;
  }
}
