package org.neustupov.javadevinterviewbot.botapi.messagecreator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.TestData.getListOfQuestion;
import static org.neustupov.javadevinterviewbot.botapi.messagecreator.ResponseMessageCreatorTest.Buttons.*;
import static org.neustupov.javadevinterviewbot.botapi.states.BotState.FILLING_SEARCH;
import static org.neustupov.javadevinterviewbot.botapi.states.BotState.SHOW_LEVEL_MENU;
import static org.neustupov.javadevinterviewbot.botapi.states.BotState.SHOW_START_MENU;
import static org.neustupov.javadevinterviewbot.botapi.states.Category.OOP;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.model.RangePair;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@SpringBootTest
@ActiveProfiles("test")
class ResponseMessageCreatorTest {

  @Autowired
  private ResponseMessageCreator messageCreator;

  @MockBean
  private UserDataCache dataCache;

  @MockBean
  private UserContext userContext;

  private List<Question> qList;

  public interface Buttons {

    String QUESTIONS = "Вопросы";
    String SEARCH = "Поиск";
    String TESTS = "Тестирование";

    String QUESTIONS_BUTTON = "buttonQuestions";
    String SEARCH_BUTTON = "buttonSearch";
    String TESTS_BUTTON = "buttonTest";
  }

  @BeforeEach
  void setUp() {
    qList = getListOfQuestion();
  }

  @Test
  void getSimplyMessage() {
    SendMessage sendMessageStart = messageCreator
        .getSimplyMessage(100L, "reply.menu", SHOW_START_MENU, false);

    assertFalse(sendMessageStart.getText().isEmpty());
    assertEquals(sendMessageStart.getText(), "С чего начнем?");

    SendMessage sendMessageLevel = messageCreator
        .getSimplyMessage(100L, "reply.level", SHOW_LEVEL_MENU, true);

    assertTrue(!sendMessageLevel.getText().isEmpty());
    assertEquals(sendMessageLevel.getText(), "Выбери уровень.");
    List<List<InlineKeyboardButton>> keyboard = ((InlineKeyboardMarkup) sendMessageLevel
        .getReplyMarkup()).getKeyboard();
    List<InlineKeyboardButton> buttons = keyboard.get(0);
    assertEquals(buttons.size(), 1);
    assertEquals(buttons.get(0).getText(),
        "⬆   Вернуться в главное меню   ⬆");
  }

  @Test
  void getSimpleMessageWithButtons() {
    when(dataCache.getUserCurrentBotState(anyLong())).thenReturn(SHOW_START_MENU);

    Map<String, String> buttonMap = new LinkedHashMap<>();
    buttonMap.put(QUESTIONS, QUESTIONS_BUTTON);
    buttonMap.put(SEARCH, SEARCH_BUTTON);
    buttonMap.put(TESTS, TESTS_BUTTON);

    SendMessage sendMessageStart = messageCreator
        .getSimpleMessageWithButtons(100L, "reply.menu", buttonMap);
    assertFalse(sendMessageStart.getText().isEmpty());
    assertEquals(sendMessageStart.getText(), "С чего начнем?");
    List<List<InlineKeyboardButton>> keyboard = ((InlineKeyboardMarkup) sendMessageStart
        .getReplyMarkup()).getKeyboard();
    List<InlineKeyboardButton> buttons = keyboard.get(0);
    assertEquals(buttons.size(), 3);
    assertEquals(buttons.get(0).getText(), "Вопросы");
    assertEquals(buttons.get(1).getText(), "Поиск");
    assertEquals(buttons.get(2).getText(), "Тестирование");
  }

  @Test
  void getMessage() {
    when(dataCache.getUserCurrentBotState(anyLong())).thenReturn(FILLING_SEARCH);

    RangePair rangePair = GenericBuilder.of(RangePair::new)
        .with(RangePair::setFrom, 0)
        .with(RangePair::setTo, 1)
        .build();

    when(dataCache.getUserContext(anyLong())).thenReturn(userContext);
    when(userContext.getRange()).thenReturn(null).thenReturn(rangePair);
    when(userContext.getCategory()).thenReturn(OOP);

    SendMessage sendMessage = messageCreator.getMessage(qList, 100L, 200, "next");

    assertFalse(sendMessage.getText().isEmpty());
    assertEquals(sendMessage.getText(), "Категория - ООП\n\n/q700 Test 1\n\n");
    List<List<InlineKeyboardButton>> keyboard = ((InlineKeyboardMarkup) sendMessage
        .getReplyMarkup()).getKeyboard();

    List<InlineKeyboardButton> paginationButtons = keyboard.get(0);
    assertEquals(paginationButtons.size(), 1);
    assertEquals(paginationButtons.get(0).getText(), "Сюда ➡");

    List<InlineKeyboardButton> backButtons = keyboard.get(1);
    assertEquals(backButtons.size(), 1);
    assertEquals(backButtons.get(0).getText(),
        "⬆   Вернуться в главное меню   ⬆");
  }

  @Test
  void getStringMap() {
    Map<String, String> buttonsMap = messageCreator
        .getStringMap(QUESTIONS, QUESTIONS_BUTTON, SEARCH, SEARCH_BUTTON, TESTS, TESTS_BUTTON);
    assertEquals(buttonsMap.size(), 3);
  }
}