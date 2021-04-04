package org.neustupov.javadevinterviewbot.botapi.handlers.lists;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.TestData.getMessage;
import static org.neustupov.javadevinterviewbot.TestData.getUser;
import static org.neustupov.javadevinterviewbot.model.BotState.SHOW_QUESTION;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.neustupov.javadevinterviewbot.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@SpringBootTest
@ActiveProfiles("test")
class QuestionHandlerTest {

  @Autowired
  private QuestionHandler questionHandler;

  @MockBean
  private QuestionRepository questionRepository;

  @MockBean
  private UserDataCache dataCache;

  @MockBean
  private UserContext userContext;

  private Message message;

  @BeforeEach
  void setUp() {
    when(dataCache.getUserContext(anyLong())).thenReturn(userContext);
    when(dataCache.getUserCurrentBotState(anyLong())).thenReturn(BotState.SHOW_QUESTION);

    message = getMessage();
    message.setText("/q01");
    message.setFrom(getUser());

    Optional<Question> questionOptional = Optional.of(GenericBuilder.of(Question::new)
            .with(Question::setLargeDescription, "Question 1")
            .build());

    when(questionRepository.findById(any())).thenReturn(questionOptional);
  }

  @Test
  void handle() {
    SendMessage sendMessage = questionHandler.handle(message);
    assertFalse(sendMessage.getText().isEmpty());
    assertEquals(sendMessage.getText(), "Question 1");

    List<List<InlineKeyboardButton>> keyboard = ((InlineKeyboardMarkup) sendMessage
        .getReplyMarkup()).getKeyboard();

    List<InlineKeyboardButton> buttons = keyboard.get(0);
    assertEquals(buttons.size(), 1);
    assertEquals(buttons.get(0).getText(), "⬆ Назад");
  }

  @Test
  void getHandlerName() {
    BotState botState = questionHandler.getHandlerName();
    assertEquals(botState, SHOW_QUESTION);
  }
}