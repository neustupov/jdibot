package org.neustupov.javadevinterviewbot;

import lombok.Setter;
import org.neustupov.javadevinterviewbot.state.State;
import org.neustupov.javadevinterviewbot.state.StateContext;
import org.neustupov.javadevinterviewbot.state.factory.StateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Setter
public class JavaDevInterviewBot extends TelegramWebhookBot {

  private String webHookPath;
  private String botUserName;
  private String botToken;

  @Autowired
  private StateContext stateContext;

  public JavaDevInterviewBot(DefaultBotOptions options) {
    super(options);
  }

  @Override
  public String getBotUsername() {
    return botUserName;
  }

  @Override
  public String getBotToken() {
    return botToken;
  }

  @Override
  public String getBotPath() {
    return webHookPath;
  }

  @Override
  public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
    if (update.getMessage() != null && update.getMessage().hasText()){
      long chatId = update.getMessage().getChatId();
      State userState = stateContext.getUserState(chatId);
      if (userState == null){
        userState = StateFactory
      }


      try {
        execute(new SendMessage(chatId, "Hi " + update.getMessage().getText()));
      } catch (TelegramApiException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
