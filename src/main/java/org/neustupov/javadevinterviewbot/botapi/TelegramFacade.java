package org.neustupov.javadevinterviewbot.botapi;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.callbacks.CallbackProcessor;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramFacade {

  BotStateContext botStateContext;
  UserDataCache userDataCache;
  CallbackProcessor callbackProcessor;

  public TelegramFacade(BotStateContext botStateContext,
      UserDataCache userDataCache,
      CallbackProcessor callbackProcessor) {
    this.botStateContext = botStateContext;
    this.userDataCache = userDataCache;
    this.callbackProcessor = callbackProcessor;
  }

  public BotApiMethod<?> handleUpdate(Update update) {
    SendMessage replyMessage = null;
    Message message = update.getMessage();

    if (update.hasCallbackQuery()) {
      CallbackQuery callbackQuery = update.getCallbackQuery();
      log.info("New CallbackQuery from User:{}, userId:{}, with daa:{}",
          callbackQuery.getFrom().getFirstName() + " " + callbackQuery.getFrom().getLastName(),
          callbackQuery.getFrom().getId(), callbackQuery.getData());
      return callbackProcessor.processCallbackQuery(callbackQuery);
    }

    if (message != null && message.hasText()) {
      log.info("New message from User:{}, chatId:{}, with text: {}",
          message.getFrom().getFirstName() + " " + message.getFrom().getLastName(),
          message.getChatId(), message.getText());
      replyMessage = handleInputMessage(message);
    }
    return replyMessage;
  }

  private SendMessage handleInputMessage(Message message) {
    String inputMsg = message.getText();
    int userId = message.getFrom().getId();
    BotState botState;

    switch (inputMsg) {
      case "/start":
        botState = BotState.SHOW_START_MENU;
        break;
      case "junior":
      case "middle":
      case "senior":
        botState = BotState.SHOW_LEVEL_MENU;
        break;
      case "oop":
      case "collection":
      case "core":
        botState = BotState.SHOW_CATEGORY_MENU;
        break;
      default:
        botState = userDataCache.getUserCurrentBotState(userId);
        break;
    }

    if (inputMsg.startsWith("/q")) {
      botState = BotState.SHOW_QUESTION;
      return botStateContext.processInputMessage(botState, message);
    }

    userDataCache.setUserCurrentBotState(userId, botState);
    return botStateContext.processInputMessage(botState, message);
  }
}
