package org.neustupov.javadevinterviewbot.botapi.buttons;

import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Buttons.BACK;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Buttons.BACK_TO_START_MENU;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Buttons.NEW_SEARCH;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.BACK_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.BACK_TO_START_MENU_BUTTON;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.NEW_SEARCH_BUTTON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class ButtonMaker {

  private static final String BUTTONS = "buttons";
  private static final String CALLBACKS = "callbacks";

  public interface Buttons{
    String BACK = "Назад";
    String NEW_SEARCH = "Новый поиск";
    String BACK_TO_START_MENU = "Вернуться в главное меню";
    String OOP = "ООП";
  }

  public interface Callbacks{
    String BACK_BUTTON = "backButton";
    String NEW_SEARCH_BUTTON = "newSearchButton";
    String BACK_TO_START_MENU_BUTTON = "backToStartMenuButton";
  }

  public InlineKeyboardMarkup getInlineMessageButtons(Map<String, List<String>> buttonNames,
      boolean needBackButton) {
    List<String> buttons = buttonNames.get(BUTTONS);
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    InlineKeyboardButton buttonQuestions = new InlineKeyboardButton().setText(buttons.get(0));
    InlineKeyboardButton buttonSearch = new InlineKeyboardButton().setText(buttons.get(1));
    InlineKeyboardButton buttonTest = new InlineKeyboardButton().setText(buttons.get(2));

    List<String> callbacks = buttonNames.get(CALLBACKS);
    buttonQuestions.setCallbackData(callbacks.get(0));
    buttonSearch.setCallbackData(callbacks.get(1));
    buttonTest.setCallbackData(callbacks.get(2));

    List<InlineKeyboardButton> menuButtons = new ArrayList<>();
    menuButtons.add(buttonQuestions);
    menuButtons.add(buttonSearch);
    menuButtons.add(buttonTest);

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

  public InlineKeyboardMarkup getBackFromSearchButtons(){
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

    InlineKeyboardButton buttonNewSearch = new InlineKeyboardButton().setText(NEW_SEARCH);
    InlineKeyboardButton buttonBaskToStartMenu = new InlineKeyboardButton().setText(BACK_TO_START_MENU);
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
}
