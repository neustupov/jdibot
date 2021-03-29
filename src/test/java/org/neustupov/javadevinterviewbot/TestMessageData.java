package org.neustupov.javadevinterviewbot;

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

}
