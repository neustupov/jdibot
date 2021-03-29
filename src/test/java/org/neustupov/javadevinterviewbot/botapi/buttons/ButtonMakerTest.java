package org.neustupov.javadevinterviewbot.botapi.buttons;

import static org.junit.jupiter.api.Assertions.*;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Buttons.*;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
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
    InlineKeyboardMarkup inlineKeyboardMarkup = buttonMaker.getBackToQuestionsButton();
    assertTrue(!inlineKeyboardMarkup.getKeyboard().isEmpty());
    assertTrue(!inlineKeyboardMarkup.getKeyboard().get(0).isEmpty());
    assertTrue(!inlineKeyboardMarkup.getKeyboard().get(0).get(0).getText().isEmpty());
    assertEquals(inlineKeyboardMarkup.getKeyboard().get(0).get(0).getText(), "\uD83D\uDD19 Назад");
  }

  @Test
  void getBackToStartMenuButton() {
    InlineKeyboardMarkup inlineKeyboardMarkup = buttonMaker.getBackToStartMenuButton();
    assertTrue(!inlineKeyboardMarkup.getKeyboard().isEmpty());
    assertTrue(!inlineKeyboardMarkup.getKeyboard().get(0).isEmpty());
    assertTrue(!inlineKeyboardMarkup.getKeyboard().get(0).get(0).getText().isEmpty());
    assertEquals(inlineKeyboardMarkup.getKeyboard().get(0).get(0).getText(), "\uD83D\uDD1D   Вернуться в главное меню   \uD83D\uDD1D");
  }

  @Test
  void getPaginationButton() {
    InlineKeyboardMarkup inlineKeyboardMarkup = buttonMaker.getPaginationButton(true, true, BotState.FILLING_SEARCH);
    assertTrue(!inlineKeyboardMarkup.getKeyboard().isEmpty());
    assertTrue(!inlineKeyboardMarkup.getKeyboard().get(0).isEmpty());
    assertEquals(inlineKeyboardMarkup.getKeyboard().get(0).size(), 2);
    assertEquals(inlineKeyboardMarkup.getKeyboard().get(0).get(0).getText(), "<-");
    assertEquals(inlineKeyboardMarkup.getKeyboard().get(0).get(1).getText(), "->");
    assertEquals(inlineKeyboardMarkup.getKeyboard().get(1).size(), 1);
    assertEquals(inlineKeyboardMarkup.getKeyboard().get(1).get(0).getText(), "\uD83D\uDD1D   Вернуться в главное меню   \uD83D\uDD1D");
  }

  private Map<String, String> getButtonNames() {
    Map<String, String> resultMap = new HashMap<>();
    resultMap.put(OOP, OOP_CATEGORY_BUTTON);
    resultMap.put(COLLECTIONS, COLLECTIONS_CATEGORY_BUTTON);
    return resultMap;
  }
}