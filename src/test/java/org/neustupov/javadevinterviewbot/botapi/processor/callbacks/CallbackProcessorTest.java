package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@SpringBootTest
@ActiveProfiles("test")
class CallbackProcessorTest {

  @Autowired
  private CallbackProcessor callbackProcessor;

  @MockBean
  private BotStateContext botStateContext;

  @Mock
  private Message message;

  private CallbackQuery callbackQuery;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    SendMessage sendMessage = GenericBuilder.of(SendMessage::new)
        .with(SendMessage::setText, "handle callback")
        .build();

    when(botStateContext.processInputMessage(any(BotState.class), any(Message.class)))
        .thenReturn(sendMessage);

    User from = GenericBuilder.of(User::new)
        .with(User::setId, 100)
        .build();
    callbackQuery = GenericBuilder.of(CallbackQuery::new)
        .with(CallbackQuery::setFrom, from)
        .with(CallbackQuery::setMessage, message)
        .with(CallbackQuery::setData, "backButton")
        .build();
  }

  @Test
  void processCallbackQuery() {
    BotApiMethod<?> botResponse = callbackProcessor.processCallbackQuery(callbackQuery);
    assertFalse(botResponse.getMethod().isEmpty());
    assertEquals(((SendMessage) botResponse).getText(), "handle callback");
  }
}