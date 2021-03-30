package org.neustupov.javadevinterviewbot.botapi.processor.messages;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.TestData.getMessage;
import static org.neustupov.javadevinterviewbot.botapi.processor.messages.MessageProcessorTest.Callbacks.*;
import static org.neustupov.javadevinterviewbot.botapi.processor.messages.MessageProcessorTest.Text.*;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.neustupov.javadevinterviewbot.repository.UserContextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@SpringBootTest
@ActiveProfiles("test")
class MessageProcessorTest {

  public interface Callbacks {

    String START = "start";
    String QUESTION = "question";
  }

  public interface Text {

    String START_TEXT = "/start";
    String QUESTION_TEXT = "/q100";
  }

  @Autowired
  private MessageProcessor messageProcessor;

  @Autowired
  private UserDataCache dataCache;

  @MockBean
  private UserContextRepository contextRepository;

  @MockBean
  private BotStateContext botStateContext;

  private Message message;

  @BeforeEach
  void setUp() {
    message = getMessage();

    User from = GenericBuilder.of(User::new)
        .with(User::setId, 100)
        .build();

    message.setFrom(from);

    Optional<UserContext> userContextOptional = Optional
        .of(GenericBuilder.of(UserContext::new).build());

    when(contextRepository.findById(anyLong())).thenReturn(userContextOptional);

    SendMessage smStart = GenericBuilder.of(SendMessage::new)
        .with(SendMessage::setText, START)
        .build();
    SendMessage smQuestion = GenericBuilder.of(SendMessage::new)
        .with(SendMessage::setText, QUESTION)
        .build();

    when(botStateContext.processInputMessage(eq(BotState.SHOW_START_MENU), any(Message.class)))
        .thenReturn(smStart);
    when(botStateContext.processInputMessage(eq(BotState.SHOW_QUESTION), any(Message.class)))
        .thenReturn(smQuestion);
  }

  @ParameterizedTest
  @MethodSource("provideButtonsForHandleMessage")
  void handleInputMessage(String text, String callback, BotState state) {
    message.setText(text);
    SendMessage sendMessage = messageProcessor.handleInputMessage(message);
    assertFalse(sendMessage.getText().isEmpty());
    assertEquals(sendMessage.getText(), callback);
    assertEquals(dataCache.getUserCurrentBotState(100), state);
  }

  private static Stream<Arguments> provideButtonsForHandleMessage() {
    return Stream.of(
        Arguments.of(START_TEXT, START, BotState.SHOW_START_MENU),
        Arguments.of(QUESTION_TEXT, QUESTION, BotState.SHOW_QUESTION)
    );
  }
}