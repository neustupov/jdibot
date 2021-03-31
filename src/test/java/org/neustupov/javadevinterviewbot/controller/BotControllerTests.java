package org.neustupov.javadevinterviewbot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.neustupov.javadevinterviewbot.TestUtil;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.neustupov.javadevinterviewbot.service.QuestionNumService;
import org.neustupov.javadevinterviewbot.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class BotControllerTests {

  private static final int CHAT_ID = 869489319;
  private static final String CONTENT_TYPE_WITH_ENCODING = "application/json;charset=UTF-8";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private QuestionService questionService;

  private Update update;
  private Message message;
  private CallbackQuery callbackQuery;
  private User botUser;

  @MockBean
  private UserDataCache dataCache;

  @MockBean
  private QuestionNumService questionNumService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    Mockito.when(questionNumService.getNext())
        .thenReturn(1L)
        .thenReturn(2L);

    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    User realUser = new User();
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

    MessageEntity entity = new MessageEntity();
    entity.setType("bot_command");
    entity.setOffset(0);
    entity.setLength(6);
    entity.setText("/start");

    message = new Message();
    message.setMessageId(100500);
    message.setFrom(realUser);
    message.setDate(1615311730);
    message.setChat(chat);
    message.setText("/start");
    message.setEntities(Collections.singletonList(entity));

    callbackQuery = new CallbackQuery();
    callbackQuery.setId("23242342342424");
    callbackQuery.setFrom(realUser);

    update = new Update();
  }

  @Test
  void onUpdateReceived() throws Exception {
    Mockito.when(dataCache.getUserCurrentBotState(anyLong()))
        .thenReturn(BotState.SHOW_START_MENU);

    update.setMessage(message);

    this.mockMvc
        .perform(post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(update)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_WITH_ENCODING));
  }

  @Test
  void startMenuTest() throws Exception {
    Mockito.when(dataCache.getUserCurrentBotState(anyLong()))
        .thenReturn(BotState.SHOW_START_MENU);

    update.setMessage(message);

    MvcResult mvcResult = this.mockMvc
        .perform(post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(update)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_WITH_ENCODING))
        .andReturn();

    SendMessage sendMessage = (SendMessage) TestUtil
        .convertJSONStringToObject(mvcResult.getResponse().getContentAsString(), SendMessage.class);

    List<InlineKeyboardButton> keyboardMarkup = ((InlineKeyboardMarkup) sendMessage
        .getReplyMarkup()).getKeyboard().get(0);
    assertEquals(sendMessage.getText(), "С чего начнем?");
    assertEquals(((InlineKeyboardMarkup) sendMessage.getReplyMarkup()).getKeyboard().size(), 1);
    assertEquals(keyboardMarkup.size(), 3);
    assertEquals(keyboardMarkup.get(0).getText(), "Вопросы");
    assertEquals(keyboardMarkup.get(1).getText(), "Поиск");
    assertEquals(keyboardMarkup.get(2).getText(), "Тестирование");
  }

  @Test
  void levelMenuTest() throws Exception {
    Mockito.when(dataCache.getUserCurrentBotState(anyLong()))
        .thenReturn(BotState.SHOW_START_MENU);

    callbackQuery.setData(QUESTIONS_BUTTON);
    callbackQuerySet();

    MvcResult mvcResult = this.mockMvc
        .perform(post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(update)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_WITH_ENCODING))
        .andReturn();

    SendMessage sendMessage = (SendMessage) TestUtil
        .convertJSONStringToObject(mvcResult.getResponse().getContentAsString(), SendMessage.class);

    List<InlineKeyboardButton> keyboardMarkup = ((InlineKeyboardMarkup) sendMessage
        .getReplyMarkup()).getKeyboard().get(0);
    assertEquals(sendMessage.getText(), "Выбери уровень.");
    assertEquals(((InlineKeyboardMarkup) sendMessage.getReplyMarkup()).getKeyboard().size(), 1);
    assertEquals(keyboardMarkup.size(), 3);
    assertEquals(keyboardMarkup.get(0).getText(), "Junior");
    assertEquals(keyboardMarkup.get(1).getText(), "Middle");
    assertEquals(keyboardMarkup.get(2).getText(), "Senior");
  }

  @Test
  void categoryMenuTest() throws Exception {
    Mockito.when(dataCache.getUserCurrentBotState(anyLong()))
        .thenReturn(BotState.SHOW_LEVEL_MENU);

    callbackQuery.setData(JUNIOR_LEVEL_BUTTON);
    callbackQuerySet();

    MvcResult mvcResult = this.mockMvc
        .perform(post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(update)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_WITH_ENCODING))
        .andReturn();

    SendMessage sendMessage = (SendMessage) TestUtil
        .convertJSONStringToObject(mvcResult.getResponse().getContentAsString(), SendMessage.class);

    List<InlineKeyboardButton> keyboardMarkup = ((InlineKeyboardMarkup) sendMessage
        .getReplyMarkup()).getKeyboard().get(0);
    List<InlineKeyboardButton> backButtons = ((InlineKeyboardMarkup) sendMessage.getReplyMarkup())
        .getKeyboard().get(1);

    assertEquals(sendMessage.getText(), "Выбери категорию.");
    assertEquals(((InlineKeyboardMarkup) sendMessage.getReplyMarkup()).getKeyboard().size(), 2);
    assertEquals(keyboardMarkup.size(), 4);
    assertEquals(keyboardMarkup.get(0).getText(), "ООП");
    assertEquals(keyboardMarkup.get(1).getText(), "Коллекции");
    assertEquals(keyboardMarkup.get(2).getText(), "Паттерны");
    assertEquals(keyboardMarkup.get(3).getText(), "Spring");

    assertEquals(backButtons.get(0).getText(),
        "⬆   Вернуться в главное меню   ⬆");
  }

  @Test
  void questionTest() throws Exception {

    questionService.save(GenericBuilder.of(Question::new)
        .with(Question::setCategory, Category.OOP)
        .with(Question::setLevel, Level.JUNIOR)
        .with(Question::setSmallDescription, "Test")
        .with(Question::setLargeDescription, "Test Test")
        .build());

    callbackQuery.setData(OOP_CATEGORY_BUTTON);
    callbackQuerySet();

    UserContext userContext = GenericBuilder.of(UserContext::new)
        .with(UserContext::setLevel, Level.JUNIOR)
        .with(UserContext::setCategory, Category.OOP)
        .build();
    when(dataCache.getUserContext(CHAT_ID)).thenReturn(userContext);
    when(dataCache.getUserCurrentBotState(CHAT_ID)).thenReturn(BotState.SHOW_QUESTION);

    callbackQuery.setData(OOP_CATEGORY_BUTTON);
    callbackQuerySet();

    MvcResult mvcResult = this.mockMvc
        .perform(post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(update)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_WITH_ENCODING))
        .andDo(print())
        .andReturn();

    SendMessage sendMessage = (SendMessage) TestUtil
        .convertJSONStringToObject(mvcResult.getResponse().getContentAsString(), SendMessage.class);
    assertEquals(sendMessage.getText(), "Категория - ООП\n\n/q1 Test\n");
  }

  private void callbackQuerySet() {
    update.setMessage(null);
    update.setUpdateId(23443223);
    message.setFrom(botUser);
    callbackQuery.setMessage(message);
    update.setCallbackQuery(callbackQuery);
  }
}