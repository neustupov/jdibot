package org.neustupov.javadevinterviewbot.botapi;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.TestMessageData.getCallbackQuery;
import static org.neustupov.javadevinterviewbot.TestMessageData.getMessage;
import static org.neustupov.javadevinterviewbot.TestMessageData.getUpdate;
import static org.neustupov.javadevinterviewbot.TestMessageData.getUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@SpringBootTest
@ActiveProfiles("test")
class TelegramFacadeTest {

  @Autowired
  private TelegramFacade telegramFacade;

  @MockBean
  private UserDataCache dataCache;

  private Update update;
  private CallbackQuery callbackQuery;
  private Message message;

  @BeforeEach
  void setUp() {
    when(dataCache.getUserCurrentBotState(anyLong())).thenReturn(BotState.SHOW_CATEGORY_MENU);

    message = getMessage();
    callbackQuery = getCallbackQuery();
    callbackQuery.setMessage(message);
    update = getUpdate();
  }

  @Test
  void handleUpdate() {
    update.setCallbackQuery(callbackQuery);
    BotApiMethod<?> callbackHandle = telegramFacade.handleUpdate(update);
    assertFalse(callbackHandle.getMethod().isEmpty());
    assertEquals(((SendMessage) callbackHandle).getText(), "Выбери категорию.");

    update.setCallbackQuery(null);
    message.setText("/start");
    message.setFrom(getUser());
    update.setMessage(message);
    BotApiMethod<?> messageHandle = telegramFacade.handleUpdate(update);
    assertFalse(messageHandle.getMethod().isEmpty());
    assertEquals(((SendMessage) messageHandle).getText(), "С чего начнем?");
  }
}