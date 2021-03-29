package org.neustupov.javadevinterviewbot.botapi.handlers.menu.springmenu;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.TestMessageData.getMessage;
import static org.neustupov.javadevinterviewbot.botapi.handlers.menu.springmenu.SpringCategoryMenuHandlerTest.Buttons.*;
import static org.neustupov.javadevinterviewbot.botapi.states.BotState.*;

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
class SpringCategoryMenuHandlerTest {

  @Autowired
  private SpringCategoryMenuHandler springCategoryMenuHandler;

  @MockBean
  private UserDataCache dataCache;

  private Message message;

  public interface Buttons {
    String SPRING_PART_1 = "Spring part 1";
    String SPRING_PART_2 = "Spring part 2";
    String SPRING_PART_3 = "Spring part 3";
  }

  @BeforeEach
  void setUp() {
    when(dataCache.getUserCurrentBotState(anyLong())).thenReturn(SHOW_CATEGORY);

    message = getMessage();
  }

  @Test
  void handle() {
    SendMessage sendMessage = springCategoryMenuHandler.handle(message);
    assertTrue(!sendMessage.getText().isEmpty());
    assertEquals(sendMessage.getText(), "Выбери категорию.");
    List<List<InlineKeyboardButton>> keyboard = ((InlineKeyboardMarkup) sendMessage
        .getReplyMarkup()).getKeyboard();

    List<InlineKeyboardButton> buttons = keyboard.get(0);
    assertEquals(buttons.size(), 3);
    assertEquals(buttons.get(0).getText(), SPRING_PART_1);
    assertEquals(buttons.get(1).getText(), SPRING_PART_2);
    assertEquals(buttons.get(2).getText(), SPRING_PART_3);
  }

  @Test
  void getHandlerName() {
    BotState botState = springCategoryMenuHandler.getHandlerName();
    assertEquals(botState, SHOW_SPRING_CATEGORY_MENU);
  }
}