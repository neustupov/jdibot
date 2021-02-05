package org.neustupov.javadevinterviewbot;

import lombok.Setter;
import org.neustupov.javadevinterviewbot.botapi.TelegramFacade;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Setter
public class JavaDevInterviewBot extends TelegramWebhookBot {

  private String webHookPath;
  private String botUserName;
  private String botToken;

  private TelegramFacade telegramFacade;

  public JavaDevInterviewBot(DefaultBotOptions options, TelegramFacade telegramFacade) {
    super(options);
    this.telegramFacade = telegramFacade;
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
    if ((update.getMessage() != null && update.getMessage().hasText()) || update.hasCallbackQuery()) {
      return telegramFacade.handleUpdate(update);
    }
    return null;
  }
}
