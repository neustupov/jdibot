package org.neustupov.javadevinterviewbot.botapi.handlers.search;

import static org.junit.jupiter.api.Assertions.*;
import static org.neustupov.javadevinterviewbot.TestMessageData.getMessage;
import static org.neustupov.javadevinterviewbot.botapi.states.BotState.SHOW_SEARCH;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@SpringBootTest
@ActiveProfiles("test")
class SearchHandlerTest {

  @Autowired
  private SearchHandler searchHandler;

  private Message message;

  @BeforeEach
  void setUp() {
    message = getMessage();
  }

  @Test
  void handle() {
    SendMessage sendMessage = searchHandler.handle(message);
    assertFalse(sendMessage.getText().isEmpty());
    assertEquals(sendMessage.getText(), "Введите фразу для поиска.");
    List<List<InlineKeyboardButton>> keyboard = ((InlineKeyboardMarkup) sendMessage
        .getReplyMarkup()).getKeyboard();

    List<InlineKeyboardButton> buttons = keyboard.get(0);
    assertEquals(buttons.size(), 1);
    assertEquals(buttons.get(0).getText(), "\uD83D\uDD1D   Вернуться в главное меню   \uD83D\uDD1D");
  }

  @Test
  void getHandlerName() {
    BotState botState = searchHandler.getHandlerName();
    assertEquals(botState, SHOW_SEARCH);
  }

}