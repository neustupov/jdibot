package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.botapi.processor.callbacks.NavigationButtonButtonCallbacksHandlingTest.Buttons.*;

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
class NavigationButtonButtonCallbacksHandlingTest {

  public interface Buttons {
    String BACK_BUTTON_CALLBACK = "backButtonCallBack";
    String BACK_TO_START_MENU_BUTTON_CALLBACK = "backToStartMenuButtonCallBack";
    String BACK_TO_CATEGORY_BUTTON_CALLBACK = "backToCategoryButtonCallBack";
    String BACK_TO_LEVEL_BUTTON_CALLBACK = "backToLevelButtonCallBack";
    String PAGINATION_BUTTON_CALLBACK = "paginationButtonCallBack";
  }

  @Autowired
  private NavigationButtonCallbacks navigationButtonCallbacks;

  @MockBean
  private BotStateContext botStateContext;

  @Spy
  private CallbackQuery callbackQuery;

  @Mock
  private Message message;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    SendMessage smCategoryOrSearchResult = GenericBuilder.of(SendMessage::new)
        .with(SendMessage::setText, BACK_BUTTON_CALLBACK)
        .build();
    SendMessage smShowStartMenu = GenericBuilder.of(SendMessage::new)
        .with(SendMessage::setText, BACK_TO_START_MENU_BUTTON_CALLBACK)
        .build();
    SendMessage smShowCategoryMenu = GenericBuilder.of(SendMessage::new)
        .with(SendMessage::setText, BACK_TO_CATEGORY_BUTTON_CALLBACK)
        .build();
    SendMessage smShowLevelMenu = GenericBuilder.of(SendMessage::new)
        .with(SendMessage::setText, BACK_TO_LEVEL_BUTTON_CALLBACK)
        .build();
    SendMessage smPagination = GenericBuilder.of(SendMessage::new)
        .with(SendMessage::setText, PAGINATION_BUTTON_CALLBACK)
        .build();

    when(botStateContext
        .processInputMessage(eq(BotState.CATEGORY_OR_SEARCH_RESULT), any(Message.class)))
        .thenReturn(smCategoryOrSearchResult);
    when(botStateContext.processInputMessage(eq(BotState.SHOW_START_MENU), any(Message.class)))
        .thenReturn(smShowStartMenu);
    when(botStateContext.processInputMessage(eq(BotState.SHOW_CATEGORY_MENU), any(Message.class)))
        .thenReturn(smShowCategoryMenu);
    when(botStateContext.processInputMessage(eq(BotState.SHOW_LEVEL_MENU), any(Message.class)))
        .thenReturn(smShowLevelMenu);
    when(botStateContext.processInputMessage(eq(BotState.PAGINATION_PAGE), any(Message.class)))
        .thenReturn(smPagination);
  }

  @ParameterizedTest
  @MethodSource("provideButtonsForHandleCallback")
  void handleCallback(ButtonCallbacks callbackData, String buttonText) {
    callbackQuery.setData(callbackData.toString());
    BotApiMethod<?> botApiMethodCategoryOrSearchResult = navigationButtonCallbacks
        .handleCallback(callbackQuery, 100, message);
    assertFalse(botApiMethodCategoryOrSearchResult.getMethod().isEmpty());
    assertEquals(((SendMessage) botApiMethodCategoryOrSearchResult).getText(), buttonText);
  }

  private static Stream<Arguments> provideButtonsForHandleCallback() {
    return Stream.of(
        Arguments.of(ButtonCallbacks.BACK_BUTTON, BACK_BUTTON_CALLBACK),
        Arguments.of(ButtonCallbacks.BACK_TO_START_MENU_BUTTON, BACK_TO_START_MENU_BUTTON_CALLBACK),
        Arguments.of(ButtonCallbacks.BACK_TO_CATEGORY_BUTTON, BACK_TO_CATEGORY_BUTTON_CALLBACK),
        Arguments.of(ButtonCallbacks.BACK_TO_LEVEL_BUTTON, BACK_TO_LEVEL_BUTTON_CALLBACK),
        Arguments.of(ButtonCallbacks.NEXT_BUTTON, PAGINATION_BUTTON_CALLBACK),
        Arguments.of(ButtonCallbacks.PREVIOUS_BUTTON, PAGINATION_BUTTON_CALLBACK)
    );
  }
}