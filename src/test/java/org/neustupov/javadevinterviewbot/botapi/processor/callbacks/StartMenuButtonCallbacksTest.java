package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.botapi.processor.callbacks.StartMenuButtonCallbacksTest.Buttons.*;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.neustupov.javadevinterviewbot.model.buttons.ButtonCallbacks;
import org.neustupov.javadevinterviewbot.repository.UserContextRepository;
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
public class StartMenuButtonCallbacksTest {

  public interface Buttons {

    String QUESTIONS_BUTTON = "buttonQuestions";
    String SEARCH_BUTTON = "buttonSearch";
    String TESTS_BUTTON = "buttonTest";

    String QUESTIONS_BUTTON_CALLBACK = "buttonQuestionsCallback";
    String SEARCH_BUTTON_CALLBACK = "buttonSearchCallback";
    String TESTS_BUTTON_CALLBACK = "buttonTestCallback";
  }

  @Autowired
  private StartMenuCallbacks startMenuCallbacks;

  @MockBean
  private BotStateContext botStateContext;

  @MockBean
  private UserContextRepository contextRepository;

  @Spy
  private CallbackQuery callbackQuery;

  @Mock
  private Message message;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    SendMessage smQuestions = GenericBuilder.of(SendMessage::new)
        .with(SendMessage::setText, QUESTIONS_BUTTON_CALLBACK)
        .build();
    SendMessage smSearch = GenericBuilder.of(SendMessage::new)
        .with(SendMessage::setText, SEARCH_BUTTON_CALLBACK)
        .build();

    when(botStateContext.processInputMessage(eq(BotState.SHOW_LEVEL_MENU), any(Message.class)))
        .thenReturn(smQuestions);
    when(botStateContext.processInputMessage(eq(BotState.SHOW_SEARCH), any(Message.class)))
        .thenReturn(smSearch);

    Optional<UserContext> userContextOptional = Optional
        .of(GenericBuilder.of(UserContext::new).build());

    when(contextRepository.findById(anyLong())).thenReturn(userContextOptional);
  }

  @ParameterizedTest
  @MethodSource("provideButtonsForHandleCallback")
  void handleCallback(ButtonCallbacks callbackData, String buttonText) {
    callbackQuery.setData(callbackData.toString());
    BotApiMethod<?> botApiMethodCategoryOrSearchResult = startMenuCallbacks
        .handleCallback(callbackQuery, 100, message);
    assertFalse(botApiMethodCategoryOrSearchResult.getMethod().isEmpty());
    assertEquals(((SendMessage) botApiMethodCategoryOrSearchResult).getText(), buttonText);
  }

  private static Stream<Arguments> provideButtonsForHandleCallback() {
    return Stream.of(
        Arguments.of(ButtonCallbacks.QUESTIONS_BUTTON, QUESTIONS_BUTTON_CALLBACK),
        Arguments.of(ButtonCallbacks.SEARCH_BUTTON, SEARCH_BUTTON_CALLBACK)
    );
  }
}