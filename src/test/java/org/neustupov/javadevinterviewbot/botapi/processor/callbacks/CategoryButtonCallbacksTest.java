package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.TestData.Buttons.*;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.model.buttons.ButtonCallbacks;
import org.neustupov.javadevinterviewbot.model.menu.Category;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.UserContext;
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
class CategoryButtonCallbacksTest {

  @Autowired
  private CategoryCallbacks categoryCallbacks;

  @Autowired
  private UserDataCache dataCache;

  @MockBean
  private BotStateContext botStateContext;

  @MockBean
  private UserContextRepository contextRepository;

  @Mock
  private CallbackQuery callbackQuery;

  @Mock
  private Message message;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    SendMessage smCategory = GenericBuilder.of(SendMessage::new)
        .with(SendMessage::setText, CATEGORY_BUTTON_CALLBACK)
        .build();
    SendMessage smSpring = GenericBuilder.of(SendMessage::new)
        .with(SendMessage::setText, SPRING_BUTTON_CALLBACK)
        .build();

    when(botStateContext.processInputMessage(eq(BotState.SHOW_CATEGORY), any(Message.class)))
        .thenReturn(smCategory);
    when(botStateContext
        .processInputMessage(eq(BotState.SHOW_SPRING_CATEGORY_MENU), any(Message.class)))
        .thenReturn(smSpring);

    Optional<UserContext> userContextOptional = Optional
        .of(GenericBuilder.of(UserContext::new).build());

    when(contextRepository.findById(anyLong())).thenReturn(userContextOptional);
  }

  @ParameterizedTest
  @MethodSource("provideButtonsForHandleCallback")
  void handleCallback(ButtonCallbacks callbackData, String buttonText, Category category) {
    BotApiMethod<?> botApiMethodCategoryOrSearchResult = categoryCallbacks
        .handleCallback(callbackQuery, callbackData, dataCache, 100, message);
    assertFalse(botApiMethodCategoryOrSearchResult.getMethod().isEmpty());
    assertEquals(((SendMessage) botApiMethodCategoryOrSearchResult).getText(), buttonText);
    assertEquals(dataCache.getUserContext(100).getCategory(), category);
  }

  private static Stream<Arguments> provideButtonsForHandleCallback() {
    return Stream.of(
        Arguments.of(ButtonCallbacks.OOP_CATEGORY_BUTTON, CATEGORY_BUTTON_CALLBACK, Category.OOP),
        Arguments.of(ButtonCallbacks.COLLECTIONS_CATEGORY_BUTTON, CATEGORY_BUTTON_CALLBACK, Category.COLLECTIONS),
        Arguments.of(ButtonCallbacks.PATTERNS_CATEGORY_BUTTON, CATEGORY_BUTTON_CALLBACK, Category.PATTERNS),
        Arguments.of(ButtonCallbacks.SPRING_BUTTON, SPRING_BUTTON_CALLBACK, Category.SPRING)
    );
  }
}