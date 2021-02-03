package org.neustupov.javadevinterviewbot.controller;

import org.neustupov.javadevinterviewbot.JavaDevInterviewBot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class BotController {

  private final JavaDevInterviewBot bot;

  public BotController(JavaDevInterviewBot bot) {
    this.bot = bot;
  }

  @PostMapping(value = "/")
  public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){
    return bot.onWebhookUpdateReceived(update);
  }
}
