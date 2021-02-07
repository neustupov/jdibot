package org.neustupov.javadevinterviewbot.botapi;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.processor.callbacks.CallbackProcessor;
import org.neustupov.javadevinterviewbot.botapi.processor.messages.MessageProcessor;
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
  MessageProcessor messageProcessor;

  public TelegramFacade(BotStateContext botStateContext,
      UserDataCache userDataCache,
      CallbackProcessor callbackProcessor,
      MessageProcessor messageProcessor) {
    this.botStateContext = botStateContext;
    this.userDataCache = userDataCache;
    this.callbackProcessor = callbackProcessor;
    this.messageProcessor = messageProcessor;
  }

  public BotApiMethod<?> handleUpdate(Update update) {
    SendMessage replyMessage = null;
    Message message = update.getMessage();

    if (update.hasCallbackQuery()) {
      CallbackQuery callbackQuery = update.getCallbackQuery();
      log.info("New CallbackQuery from User:{}, userId:{}, with data:{}",
          callbackQuery.getFrom().getFirstName() + " " + callbackQuery.getFrom().getLastName(),
          callbackQuery.getFrom().getId(), callbackQuery.getData());
      return callbackProcessor.processCallbackQuery(callbackQuery);
    }

    if (message != null && message.hasText()) {
      log.info("New message from User:{}, chatId:{}, with text: {}",
          message.getFrom().getFirstName() + " " + message.getFrom().getLastName(),
          message.getChatId(), message.getText());
      replyMessage = messageProcessor.handleInputMessage(message);
    }
    return replyMessage;
  }
}
