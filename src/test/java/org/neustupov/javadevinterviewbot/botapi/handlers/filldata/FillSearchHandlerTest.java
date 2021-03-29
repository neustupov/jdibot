package org.neustupov.javadevinterviewbot.botapi.handlers.filldata;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.botapi.states.BotState.FILLING_SEARCH;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.neustupov.javadevinterviewbot.service.QuestionNumService;
import org.neustupov.javadevinterviewbot.service.QuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@SpringBootTest
@ActiveProfiles("test")
class FillSearchHandlerTest {

  @Autowired
  private FillSearchHandler fillSearchHandler;
  @Autowired
  private QuestionServiceImpl questionService;

  @MockBean
  private QuestionNumService questionNumService;

  @MockBean
  private UserDataCache dataCache;

  private Message message;
  private User realUser;
  private User botUser;

  @BeforeEach
  void setUp() {
    when(questionNumService.getNext())
        .thenReturn(1L)
        .thenReturn(2L);

    UserContext userContext = GenericBuilder.of(UserContext::new)
        .with(UserContext::setSearchField, "From bot")
        .build();

    when(dataCache.getUserContext(anyLong())).thenReturn(userContext);
    when(dataCache.getUserCurrentBotState(anyLong())).thenReturn(FILLING_SEARCH);

    realUser = new User();
    realUser.setId(1020);
    realUser.setFirstName("Vova");
    realUser.setIsBot(false);
    realUser.setLastName("Pupkin");
    realUser.setLanguageCode("ru");

    botUser = new User();
    botUser.setId(456);
    botUser.setFirstName("JavaDevInterBot");
    botUser.setIsBot(true);
    botUser.setUserName("JavaDevInterBot");

    Chat chat = new Chat();
    chat.setId(869489319L);
    chat.setType("private");
    chat.setFirstName("Vova");
    chat.setLastName("Pupkin");

    message = new Message();
    message.setMessageId(100500);
    message.setDate(1615311730);
    message.setChat(chat);

    Question q1 = GenericBuilder.of(Question::new)
        .with(Question::setSmallDescription, "From user")
        .build();

    Question q2 = GenericBuilder.of(Question::new)
        .with(Question::setSmallDescription, "From bot")
        .build();

    questionService.save(q1);
    questionService.save(q2);
  }

  @Test
  void handleFromBot() {
    message.setFrom(botUser);
    SendMessage sendMessage = fillSearchHandler.handle(message);
    assertTrue(!sendMessage.getText().isEmpty());
    assertEquals(sendMessage.getText(), "/q2 From bot\n");
  }

  @Test
  void handleFromUser() {
    message.setFrom(realUser);
    message.setText("From user");
    SendMessage sendMessage = fillSearchHandler.handle(message);
    assertTrue(!sendMessage.getText().isEmpty());
    assertEquals(sendMessage.getText(), "/q1 From user\n");
  }

  @Test
  void getHandlerName() {
    BotState botState = fillSearchHandler.getHandlerName();
    assertEquals(botState, FILLING_SEARCH);
  }
}