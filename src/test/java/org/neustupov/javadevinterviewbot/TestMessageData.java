package org.neustupov.javadevinterviewbot;

import java.util.Arrays;
import java.util.List;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.Question;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class TestMessageData {

  public static Update getUpdate(){
    return GenericBuilder.of(Update::new)
        .with(Update::setUpdateId, 123)
        .build();
  }

  public static CallbackQuery getCallbackQuery(){
    return GenericBuilder.of(CallbackQuery::new)
        .with(CallbackQuery::setId, "100500")
        .with(CallbackQuery::setData, "buttonJunior")
        .with(CallbackQuery::setFrom, getUser())
        .build();
  }

  public static Message getMessage(){
    Chat chat = new Chat();
    chat.setId(869489319L);
    chat.setType("private");
    chat.setFirstName("Vova");
    chat.setLastName("Pupkin");

    Message message = new Message();
    message.setMessageId(100500);
    message.setDate(1615311730);
    message.setChat(chat);

    return message;
  }

  public static User getUser() {
    return GenericBuilder.of(User::new)
        .with(User::setId, 100)
        .build();
  }

  public static List<Question> getListOfQuestion() {
    return Arrays.asList(GenericBuilder.of(Question::new)
            .with(Question::setSmallDescription, "Test 1")
            .build(),
        GenericBuilder.of(Question::new)
            .with(Question::setSmallDescription, "Test 2")
            .build());
  }

}
