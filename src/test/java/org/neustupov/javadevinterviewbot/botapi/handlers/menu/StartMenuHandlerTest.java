package org.neustupov.javadevinterviewbot.botapi.handlers.menu;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.TestMessageData.getMessage;
import static org.neustupov.javadevinterviewbot.botapi.handlers.menu.StartMenuHandlerTest.Buttons.*;
import static org.neustupov.javadevinterviewbot.botapi.states.BotState.SHOW_START_MENU;

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
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@SpringBootTest
@ActiveProfiles("test")
class StartMenuHandlerTest {

  @Autowired
  private StartMenuHandler startMenuHandler;

  @MockBean
  private UserDataCache dataCache;

  private Message message;

  public interface Buttons {
    String QUESTIONS = "Вопросы";
    String SEARCH = "Поиск";
    String TESTS = "Тестирование";
  }

  @BeforeEach
  void setUp() {
    when(dataCache.getUserCurrentBotState(anyLong())).thenReturn(SHOW_START_MENU);

    message = getMessage();
  }

  @Test
  void handle() {
    SendMessage sendMessage = startMenuHandler.handle(message);
    assertTrue(!sendMessage.getText().isEmpty());
    assertEquals(sendMessage.getText(), "С чего начнем?");
    List<List<InlineKeyboardButton>> keyboard = ((InlineKeyboardMarkup) sendMessage
        .getReplyMarkup()).getKeyboard();

    List<InlineKeyboardButton> buttons = keyboard.get(0);
    assertEquals(buttons.size(), 3);
    assertEquals(buttons.get(0).getText(), QUESTIONS);
    assertEquals(buttons.get(1).getText(), SEARCH);
    assertEquals(buttons.get(2).getText(), TESTS);
  }

  @Test
  void getHandlerName() {
    BotState botState = startMenuHandler.getHandlerName();
    assertEquals(botState, SHOW_START_MENU);
  }
}