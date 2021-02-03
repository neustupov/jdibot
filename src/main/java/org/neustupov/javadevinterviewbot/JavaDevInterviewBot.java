package org.neustupov.javadevinterviewbot;

import lombok.Setter;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Setter
public class JavaDevInterviewBot extends TelegramWebhookBot {

  private String webHookPath;
  private String botUserName;
  private String botToken;

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
      long chat_id = update.getMessage().getChatId();
    }
    return null;
  }
}
