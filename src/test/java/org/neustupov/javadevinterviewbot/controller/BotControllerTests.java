package org.neustupov.javadevinterviewbot.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@SpringBootTest
@AutoConfigureMockMvc
class BotControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  private Update update;

  @MockBean
  private UserDataCache dataCache;

  @BeforeEach
  void setup() {
    User user = new User();
    user.setId(1020);
    user.setFirstName("Vova");
    user.setIsBot(false);
    user.setLastName("Pupkin");
    user.setLanguageCode("ru");

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

    Message message = new Message();
    message.setMessageId(100500);
    message.setFrom(user);
    message.setDate(1615311730);
    message.setChat(chat);
    message.setText("/start");
    message.setEntities(Collections.singletonList(entity));

    update = new Update();
    update.setMessage(message);
  }

  @Test
  void onUpdateReceived() throws Exception {

    Mockito.when(dataCache.getUserCurrentBotState(Mockito.anyLong())).thenReturn(BotState.SHOW_START_MENU);

    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    byte[] updateBytes = mapper.writeValueAsBytes(update);

    this.mockMvc
        .perform(post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateBytes))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  void startMenuTest(){

  }

  void levelMenuTest(){

  }

  void categoryMenuTest(){

  }

  void questionMenuTest(){

  }
}