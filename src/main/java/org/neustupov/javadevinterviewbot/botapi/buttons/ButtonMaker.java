package org.neustupov.javadevinterviewbot.botapi.buttons;

import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Buttons.BACK;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Buttons.BACK_TO_START_MENU;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Buttons.NEW_SEARCH;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Buttons.NEXT;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Buttons.PREVIOUS;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.BACK_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.BACK_TO_START_MENU_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.NEW_SEARCH_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.NEXT_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.PREVIOUS_BUTTON;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class ButtonMaker {

  public interface Buttons {

    String BACK = "Назад";
    String NEW_SEARCH = "Новый поиск";
    String BACK_TO_START_MENU = "Вернуться в главное меню";

    String OOP = "ООП";
    String COLLECTIONS = "Коллекции";
    String PATTERNS = "Паттерны";

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

    String OOP_CATEGORY_BUTTON = "buttonOOP";
    String COLLECTIONS_CATEGORY_BUTTON = "buttonCollections";
    String PATTERNS_CATEGORY_BUTTON = "buttonPatterns";

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
      boolean needBackButton) {
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<InlineKeyboardButton> menuButtons = new ArrayList<>();
    buttonMap.keySet()
        .forEach(key -> menuButtons
            .add(new InlineKeyboardButton().setText(key).setCallbackData(buttonMap.get(key))));
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    rows.add(menuButtons);
    if (needBackButton) {
      rows.add(getSimpleBackButton());
    }
    inlineKeyboardMarkup.setKeyboard(rows);
    return inlineKeyboardMarkup;
  }

  public InlineKeyboardMarkup getBackButton() {
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    rows.add(getSimpleBackButton());
    inlineKeyboardMarkup.setKeyboard(rows);
    return inlineKeyboardMarkup;
  }

  private List<InlineKeyboardButton> getSimpleBackButton() {
    InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(BACK);
    buttonBack.setCallbackData(BACK_BUTTON);
    List<InlineKeyboardButton> backButtons = new ArrayList<>();
    backButtons.add(buttonBack);
    return backButtons;
  }

  public InlineKeyboardMarkup getBackFromSearchButtons() {
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

    InlineKeyboardButton buttonNewSearch = new InlineKeyboardButton().setText(NEW_SEARCH);
    InlineKeyboardButton buttonBaskToStartMenu = new InlineKeyboardButton()
        .setText(BACK_TO_START_MENU);
    buttonNewSearch.setCallbackData(NEW_SEARCH_BUTTON);
    buttonBaskToStartMenu.setCallbackData(BACK_TO_START_MENU_BUTTON);

    List<InlineKeyboardButton> menuButtonsRowFirst = new ArrayList<>();
    menuButtonsRowFirst.add(buttonNewSearch);
    List<InlineKeyboardButton> menuButtonsRowSecond = new ArrayList<>();
    menuButtonsRowSecond.add(buttonBaskToStartMenu);

    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    rows.add(menuButtonsRowFirst);
    rows.add(menuButtonsRowSecond);

    inlineKeyboardMarkup.setKeyboard(rows);

    return inlineKeyboardMarkup;
  }

  public InlineKeyboardMarkup getPaginationButton(boolean previous, boolean next) {
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();

    List<InlineKeyboardButton> paginationButton = new ArrayList<>();
    if (previous) {
      InlineKeyboardButton buttonPrevious = new InlineKeyboardButton().setText(PREVIOUS);
      buttonPrevious.setCallbackData(PREVIOUS_BUTTON);
      paginationButton.add(buttonPrevious);
    }
    if (next) {
      InlineKeyboardButton buttonNext = new InlineKeyboardButton().setText(NEXT);
      buttonNext.setCallbackData(NEXT_BUTTON);
      paginationButton.add(buttonNext);
    }
    rows.add(paginationButton);
    rows.add(getSimpleBackButton());
    inlineKeyboardMarkup.setKeyboard(rows);
    return inlineKeyboardMarkup;
  }

  public Map<String, String> getStringMap(String first, String firstCallback,
      String second, String secondCallback, String third,
      String thirdCallback) {
    Map<String, String> buttonMap = new LinkedHashMap<>();
    buttonMap.put(first, firstCallback);
    buttonMap.put(second, secondCallback);
    buttonMap.put(third, thirdCallback);
    return buttonMap;
  }
}
