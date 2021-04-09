package org.neustupov.javadevinterviewbot.controller;

import org.neustupov.javadevinterviewbot.JavaDevInterviewBot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Контроллер бота
 */
@RestController
public class BotController {

  /**
   * Бин бота
   */
  private final JavaDevInterviewBot bot;

  public BotController(JavaDevInterviewBot bot) {
    this.bot = bot;
  }

  /**
   * Обрабатывает все сообщения к боту
   *
   * @param update Update
   * @return Ответ приложения
   */
  @PostMapping(value = "/", produces = "application/json;charset=UTF-8")
  public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
    return bot.onWebhookUpdateReceived(update);
  }
}
