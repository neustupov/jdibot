package org.neustupov.javadevinterviewbot;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.TestData.getCallbackQuery;
import static org.neustupov.javadevinterviewbot.TestData.getMessage;
import static org.neustupov.javadevinterviewbot.TestData.getUpdate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@SpringBootTest
@ActiveProfiles("test")
class JavaDevInterviewBotTest {

  @Autowired
  private JavaDevInterviewBot javaDevInterviewBot;

  @MockBean
  private UserDataCache dataCache;

  private Update update;
  private CallbackQuery callbackQuery;

  @BeforeEach
  void setUp() {
    Message message = getMessage();
    callbackQuery = getCallbackQuery();
    callbackQuery.setMessage(message);
    update = getUpdate();

    when(dataCache.getUserCurrentBotState(anyLong())).thenReturn(BotState.SHOW_CATEGORY_MENU);

    Message responseMessage = new Message();
    responseMessage.setText("response");
  }

  @Test
  void onWebhookUpdateReceived() {
    update.setCallbackQuery(callbackQuery);
    BotApiMethod<?> handle = javaDevInterviewBot.onWebhookUpdateReceived(update);
    assertFalse(handle.getMethod().isEmpty());
    assertEquals(((SendMessage) handle).getText(), "Выбери категорию.");
  }
}