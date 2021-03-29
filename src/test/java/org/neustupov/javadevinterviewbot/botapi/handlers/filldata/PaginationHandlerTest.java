package org.neustupov.javadevinterviewbot.botapi.handlers.filldata;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.botapi.states.BotState.FILLING_SEARCH;
import static org.neustupov.javadevinterviewbot.botapi.states.BotState.PAGINATION_PAGE;
import static org.neustupov.javadevinterviewbot.botapi.states.BotState.SHOW_CATEGORY;
import static org.neustupov.javadevinterviewbot.botapi.states.Category.OOP;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.model.RangePair;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.neustupov.javadevinterviewbot.repository.QuestionRepositoryMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@SpringBootTest
@ActiveProfiles("test")
class PaginationHandlerTest {

  @Autowired
  private PaginationHandler paginationHandler;

  @MockBean
  private UserDataCache dataCache;

  @MockBean
  private QuestionRepositoryMongo questionRepository;

  @MockBean
  private UserContext userContext;

  private Message message;

  @BeforeEach
  void setUp() {
    when(dataCache.getUserContext(anyLong())).thenReturn(userContext);

    Chat chat = new Chat();
    chat.setId(869489319L);
    chat.setType("private");
    chat.setFirstName("Vova");
    chat.setLastName("Pupkin");

    message = new Message();
    message.setMessageId(100500);
    message.setDate(1615311730);
    message.setChat(chat);

    List<Question> qListForSearch = Arrays.asList(GenericBuilder.of(Question::new)
            .with(Question::setSmallDescription, "For Search 1")
            .build(),
        GenericBuilder.of(Question::new)
            .with(Question::setSmallDescription, "For Search 2")
            .build());

    List<Question> qListForCategory = Arrays.asList(GenericBuilder.of(Question::new)
            .with(Question::setSmallDescription, "For Category 1")
            .build(),
        GenericBuilder.of(Question::new)
            .with(Question::setSmallDescription, "For Category 2")
            .build());

    when(questionRepository.search(anyString())).thenReturn(qListForSearch);
    when(questionRepository.getAllByCategoryAndLevel(any(), any()))
        .thenReturn(qListForCategory);

    RangePair rangePair = GenericBuilder.of(RangePair::new)
        .with(RangePair::setFrom, 0)
        .with(RangePair::setTo, 1)
        .build();

    when(userContext.getRoute()).thenReturn("next");
    when(userContext.getRange())
        .thenReturn(null)
        .thenReturn(rangePair);
  }

  @Test
  void handleCategory() {
    when(dataCache.getUserCurrentBotState(anyLong())).thenReturn(SHOW_CATEGORY);
    when(userContext.getCategory()).thenReturn(OOP);

    SendMessage sendMessage = paginationHandler.handle(message);
    assertTrue(!sendMessage.getText().isEmpty());
    assertEquals(sendMessage.getText(), "Категория - ООП\n\n/q0 For Category 1\n");

    List<List<InlineKeyboardButton>> keyboard = ((InlineKeyboardMarkup) sendMessage
        .getReplyMarkup()).getKeyboard();

    List<InlineKeyboardButton> paginationButtons = keyboard.get(0);
    assertEquals(paginationButtons.size(), 1);
    assertEquals(paginationButtons.get(0).getText(), "->");

    List<InlineKeyboardButton> backButtons = keyboard.get(1);
    assertEquals(backButtons.size(), 1);
    assertEquals(backButtons.get(0).getText(),"Вернуться к категориям");
  }

  @Test
  void handleSearch() {
    when(dataCache.getUserCurrentBotState(anyLong())).thenReturn(FILLING_SEARCH);

    when(userContext.getSearchField()).thenReturn("SearchField");

    SendMessage sendMessage = paginationHandler.handle(message);
    assertTrue(!sendMessage.getText().isEmpty());
    assertEquals(sendMessage.getText(), "/q0 For Search 1\n");

    List<List<InlineKeyboardButton>> keyboard = ((InlineKeyboardMarkup) sendMessage
        .getReplyMarkup()).getKeyboard();

    List<InlineKeyboardButton> paginationButtons = keyboard.get(0);
    assertEquals(paginationButtons.size(), 1);
    assertEquals(paginationButtons.get(0).getText(), "->");

    List<InlineKeyboardButton> backButtons = keyboard.get(1);
    assertEquals(backButtons.size(), 1);
    assertEquals(backButtons.get(0).getText(),
        "\uD83D\uDD1D   Вернуться в главное меню   \uD83D\uDD1D");
  }

  @Test
  void getHandlerName() {
    BotState botState = paginationHandler.getHandlerName();
    assertEquals(botState, PAGINATION_PAGE);
  }
}