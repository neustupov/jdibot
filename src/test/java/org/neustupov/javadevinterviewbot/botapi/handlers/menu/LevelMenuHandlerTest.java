package org.neustupov.javadevinterviewbot.botapi.handlers.menu;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.TestData.Buttons.*;
import static org.neustupov.javadevinterviewbot.TestData.getMessage;
import static org.neustupov.javadevinterviewbot.model.BotState.SHOW_LEVEL_MENU;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.model.BotState;
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
class LevelMenuHandlerTest {

  @Autowired
  private LevelMenuHandler levelMenuHandler;

  @MockBean
  private UserDataCache dataCache;

  private Message message;

  @BeforeEach
  void setUp() {
    when(dataCache.getUserCurrentBotState(anyLong())).thenReturn(SHOW_LEVEL_MENU);

    message = getMessage();
  }

  @Test
  void handle() {
    SendMessage sendMessage = levelMenuHandler.handle(message);
    assertFalse(sendMessage.getText().isEmpty());
    assertEquals(sendMessage.getText(), "Выбери уровень.");
    List<List<InlineKeyboardButton>> keyboard = ((InlineKeyboardMarkup) sendMessage
        .getReplyMarkup()).getKeyboard();

    List<InlineKeyboardButton> buttons = keyboard.get(0);
    assertEquals(buttons.size(), 3);
    assertEquals(buttons.get(0).getText(), JUNIOR);
    assertEquals(buttons.get(1).getText(), MIDDLE);
    assertEquals(buttons.get(2).getText(), SENIOR);
  }

  @Test
  void getHandlerName() {
    BotState botState = levelMenuHandler.getHandlerName();
    assertEquals(botState, SHOW_LEVEL_MENU);
  }
}