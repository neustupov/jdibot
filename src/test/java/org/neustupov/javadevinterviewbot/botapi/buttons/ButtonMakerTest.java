package org.neustupov.javadevinterviewbot.botapi.buttons;

import static org.junit.jupiter.api.Assertions.*;
import static org.neustupov.javadevinterviewbot.TestData.Buttons.COLLECTIONS;
import static org.neustupov.javadevinterviewbot.TestData.Buttons.OOP;
import static org.neustupov.javadevinterviewbot.model.buttons.ButtonCallbacks.COLLECTIONS_CATEGORY_BUTTON;
import static org.neustupov.javadevinterviewbot.model.buttons.ButtonCallbacks.OOP_CATEGORY_BUTTON;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@SpringBootTest
@ActiveProfiles("test")
class ButtonMakerTest {

  @Autowired
  private ButtonMaker buttonMaker;

  private Map<String, String> buttonMap;

  @BeforeEach
  void setUp() {
    buttonMap = getButtonNames();
  }

  @Test
  void getInlineMessageButtons() {
    InlineKeyboardMarkup inlineKeyboardMarkup = buttonMaker.getInlineMessageButtons(buttonMap, BotState.SHOW_CATEGORY_MENU);
    assertTrue(!inlineKeyboardMarkup.getKeyboard().isEmpty());
  }

  @Test
  void getBackToQuestionsButton() {
    InlineKeyboardMarkup inlineKeyboardMarkup = buttonMaker.getBackButton();
    assertFalse(inlineKeyboardMarkup.getKeyboard().isEmpty());
    assertFalse(inlineKeyboardMarkup.getKeyboard().get(0).isEmpty());
    assertFalse(inlineKeyboardMarkup.getKeyboard().get(0).get(0).getText().isEmpty());
    assertEquals(inlineKeyboardMarkup.getKeyboard().get(0).get(0).getText(), "⬆ Назад");
  }

  @Test
  void getBackToStartMenuButton() {
    InlineKeyboardMarkup inlineKeyboardMarkup = buttonMaker.getBackToStartMenuButton();
    assertFalse(inlineKeyboardMarkup.getKeyboard().isEmpty());
    assertFalse(inlineKeyboardMarkup.getKeyboard().get(0).isEmpty());
    assertFalse(inlineKeyboardMarkup.getKeyboard().get(0).get(0).getText().isEmpty());
    assertEquals(inlineKeyboardMarkup.getKeyboard().get(0).get(0).getText(), "⬆   Вернуться в главное меню   ⬆");
  }

  @Test
  void getPaginationButton() {
    InlineKeyboardMarkup inlineKeyboardMarkup = buttonMaker.getPaginationButton(true, true, BotState.FILLING_SEARCH);
    assertFalse(inlineKeyboardMarkup.getKeyboard().isEmpty());
    assertFalse(inlineKeyboardMarkup.getKeyboard().get(0).isEmpty());
    assertEquals(inlineKeyboardMarkup.getKeyboard().get(0).size(), 2);
    assertEquals(inlineKeyboardMarkup.getKeyboard().get(0).get(0).getText(), "⬅ Туда");
    assertEquals(inlineKeyboardMarkup.getKeyboard().get(0).get(1).getText(), "Сюда ➡");
    assertEquals(inlineKeyboardMarkup.getKeyboard().get(1).size(), 1);
    assertEquals(inlineKeyboardMarkup.getKeyboard().get(1).get(0).getText(), "⬆   Вернуться в главное меню   ⬆");
  }

  private Map<String, String> getButtonNames() {
    Map<String, String> resultMap = new HashMap<>();
    resultMap.put(OOP, OOP_CATEGORY_BUTTON.toString());
    resultMap.put(COLLECTIONS, COLLECTIONS_CATEGORY_BUTTON.toString());
    return resultMap;
  }
}