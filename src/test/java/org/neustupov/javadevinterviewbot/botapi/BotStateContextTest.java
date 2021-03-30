package org.neustupov.javadevinterviewbot.botapi;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.TestData.getMessage;

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

@SpringBootTest
@ActiveProfiles("test")
class BotStateContextTest {

  @Autowired
  private BotStateContext botStateContext;

  @MockBean
  private UserDataCache dataCache;

  private Message message;

  @BeforeEach
  void setUp() {
    message = getMessage();
    message.setText("/start");

    when(dataCache.getUserCurrentBotState(anyLong())).thenReturn(BotState.SHOW_START_MENU);
  }

  @Test
  void processInputMessage() {
    SendMessage sendMessage = botStateContext.processInputMessage(BotState.SHOW_START_MENU, message);
    assertFalse(sendMessage.getText().isEmpty());
    assertEquals(sendMessage.getText(), "С чего начнем?");
  }
}