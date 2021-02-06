package org.neustupov.javadevinterviewbot.botapi.buttons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class ButtonMaker {

  public InlineKeyboardMarkup getInlineMessageButtons(Map<String, List<String>> buttonNames,
      boolean needBackButton) {
    List<String> buttons = buttonNames.get("buttons");
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    InlineKeyboardButton buttonQuestions = new InlineKeyboardButton().setText(buttons.get(0));
    InlineKeyboardButton buttonSearch = new InlineKeyboardButton().setText(buttons.get(1));
    InlineKeyboardButton buttonTest = new InlineKeyboardButton().setText(buttons.get(2));

    List<String> callbacks = buttonNames.get("callbacks");
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

  public List<InlineKeyboardButton> getSimpleBackButton() {
    InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText("Назад");
    buttonBack.setCallbackData("backButton");
    List<InlineKeyboardButton> backButtons = new ArrayList<>();
    backButtons.add(buttonBack);
    return backButtons;
  }
}
