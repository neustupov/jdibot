package org.neustupov.javadevinterviewbot.botapi.handlers.menu;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.TestData.getMessage;
import static org.neustupov.javadevinterviewbot.botapi.handlers.menu.CategoryMenuHandlerTest.Buttons.*;
import static org.neustupov.javadevinterviewbot.botapi.states.BotState.SHOW_CATEGORY;
import static org.neustupov.javadevinterviewbot.botapi.states.BotState.SHOW_CATEGORY_MENU;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@SpringBootTest
@ActiveProfiles("test")
class CategoryMenuHandlerTest {

  @Autowired
  private CategoryMenuHandler categoryMenuHandler;

  @MockBean
  private UserDataCache dataCache;

  private Message message;

  public interface Buttons {
    String OOP = "ООП";
    String COLLECTIONS = "Коллекции";
    String PATTERNS = "Паттерны";
    String SPRING = "Spring";
  }

  @BeforeEach
  void setUp() {
    when(dataCache.getUserCurrentBotState(anyLong())).thenReturn(SHOW_CATEGORY);

    message = getMessage();
  }

  @Test
  void handle() {
    SendMessage sendMessage = categoryMenuHandler.handle(message);
    assertFalse(sendMessage.getText().isEmpty());
    assertEquals(sendMessage.getText(), "Выбери категорию.");
    List<List<InlineKeyboardButton>> keyboard = ((InlineKeyboardMarkup) sendMessage
        .getReplyMarkup()).getKeyboard();

    List<InlineKeyboardButton> buttons = keyboard.get(0);
    assertEquals(buttons.size(), 4);
    assertEquals(buttons.get(0).getText(), OOP);
    assertEquals(buttons.get(1).getText(), COLLECTIONS);
    assertEquals(buttons.get(2).getText(), PATTERNS);
    assertEquals(buttons.get(3).getText(), SPRING);
  }

  @Test
  void getHandlerName() {
    BotState botState = categoryMenuHandler.getHandlerName();
    assertEquals(botState, SHOW_CATEGORY_MENU);
  }
}