package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.buttons.ButtonCallbacks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@SpringBootTest
@ActiveProfiles("test")
class NavigationButtonButtonCallbacksTest {

  @Autowired
  private NavigationButtonCallbacks navigationButtonCallbacks;

  @Spy
  private CallbackQuery callbackQuery;
  private Message message;

  @MockBean
  BotStateContext botStateContext;

  @BeforeEach
  void setUp() {
    navigationButtonCallbacks.setBotStateContext(botStateContext);
    message = GenericBuilder.of(Message::new).build();

    SendMessage isCategorySearchResult = GenericBuilder.of(SendMessage::new)
        .with(SendMessage::setText, "Is category search result").build();
    SendMessage isShowStartMenu = GenericBuilder.of(SendMessage::new)
        .with(SendMessage::setText, "Is show start menu").build();

    Mockito.when(botStateContext
        .processInputMessage(eq(BotState.CATEGORY_OR_SEARCH_RESULT), Mockito.any(Message.class)))
        .thenReturn(isCategorySearchResult);
    Mockito.when(botStateContext
        .processInputMessage(eq(BotState.SHOW_START_MENU), Mockito.any(Message.class)))
        .thenReturn(isShowStartMenu);
  }

  @Test
  void handleCallback() {
    callbackQuery.setData(ButtonCallbacks.BACK_BUTTON.toString());
    BotApiMethod<?> backButtonResponse = navigationButtonCallbacks
        .handleCallback(callbackQuery, 100500, message);
    assertFalse(backButtonResponse.getMethod().isEmpty());
    assertEquals(backButtonResponse.getMethod(), "sendmessage");
    assertEquals(((SendMessage) backButtonResponse).getText(), "Is category search result");

    callbackQuery.setData(ButtonCallbacks.BACK_TO_START_MENU_BUTTON.toString());
    BotApiMethod<?> backToStartMenuButtonResponse = navigationButtonCallbacks
        .handleCallback(callbackQuery, 100500, message);
    assertEquals(backButtonResponse.getMethod(), "sendmessage");
    assertEquals(((SendMessage) backToStartMenuButtonResponse).getText(), "Is show start menu");
  }
}