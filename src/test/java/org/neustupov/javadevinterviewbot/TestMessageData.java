package org.neustupov.javadevinterviewbot;

import java.util.Arrays;
import java.util.List;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.Question;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

public class TestMessageData {

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

  public static List<Question> getListOfQuestion() {
    return Arrays.asList(GenericBuilder.of(Question::new)
            .with(Question::setSmallDescription, "Test 1")
            .build(),
        GenericBuilder.of(Question::new)
            .with(Question::setSmallDescription, "Test 2")
            .build());
  }

}
