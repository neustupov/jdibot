package org.neustupov.javadevinterviewbot.botapi.handlers.common;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.botapi.states.BotState.CATEGORY_OR_SEARCH_RESULT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.botapi.handlers.filldata.FillSearchHandler;
import org.neustupov.javadevinterviewbot.botapi.handlers.lists.CategoryListsHandler;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

@SpringBootTest
@ActiveProfiles("test")
class CategoryOrSearchResultHandlerTest {

  @Autowired
  private CategoryOrSearchResultHandler categoryOrSearchResultHandler;

  @MockBean
  private CategoryListsHandler categoryListsHandler;

  @MockBean
  private FillSearchHandler fillSearchHandler;

  @MockBean
  private UserDataCache userDataCache;

  private UserContext userContext;
  private SendMessage sm;
  private Chat chat;

  @BeforeEach
  void setUp() {
    userContext = GenericBuilder.of(UserContext::new)
        .with(UserContext::setUserId, 100L)
        .build();

    when(userDataCache.getUserContext(100)).thenReturn(userContext);

    sm = GenericBuilder.of(SendMessage::new).build();
    when(fillSearchHandler.handle(any(Message.class))).thenReturn(sm);
    when(categoryListsHandler.handle(any(Message.class))).thenReturn(sm);

    chat = GenericBuilder.of(Chat::new).with(Chat::setId, 100L).build();
  }

  @Test
  void handleFillSearch() {
    userContext.setSearchField("abc");
    sm.setText("handleFillSearch");

    SendMessage sendMessage = categoryOrSearchResultHandler.handle(GenericBuilder.of(Message::new)
        .with(Message::setChat, chat).build());
    assertEquals(sendMessage.getText(), "handleFillSearch");
  }

  @Test
  void handleCategoryLists() {
    userContext.setCategory(Category.OOP);
    sm.setText("handleCategoryLists");

    SendMessage sendMessage = categoryOrSearchResultHandler.handle(GenericBuilder.of(Message::new)
        .with(Message::setChat, chat).build());
    assertEquals(sendMessage.getText(), "handleCategoryLists");
  }

  @Test
  void getHandlerName() {
    BotState botState = categoryOrSearchResultHandler.getHandlerName();
    assertEquals(botState, CATEGORY_OR_SEARCH_RESULT);
  }
}