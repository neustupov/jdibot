package org.neustupov.javadevinterviewbot.botapi;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.processor.callbacks.CallbackProcessor;
import org.neustupov.javadevinterviewbot.botapi.processor.messages.MessageProcessor;
import org.neustupov.javadevinterviewbot.model.BotResponseData;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.MessageIdKeeper;
import org.neustupov.javadevinterviewbot.service.MessageIdKeeperService;
import org.springframework.context.annotation.Lazy;
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

  CallbackProcessor callbackProcessor;
  MessageProcessor messageProcessor;
  MessageIdKeeperService messageIdKeeperService;

  public TelegramFacade(
      CallbackProcessor callbackProcessor,
      @Lazy MessageProcessor messageProcessor,
      MessageIdKeeperService messageIdKeeperService) {
    this.callbackProcessor = callbackProcessor;
    this.messageProcessor = messageProcessor;
    this.messageIdKeeperService = messageIdKeeperService;
  }

  public BotResponseData handleUpdate(Update update) {
    Message message = update.getMessage();
    BotResponseData botResponseData = null;

    if (update.hasCallbackQuery()) {
      CallbackQuery callbackQuery = update.getCallbackQuery();
      long chatId = callbackQuery.getMessage().getChatId();
      MessageIdKeeper messageIdKeeper = messageIdKeeperService.getKeeperByChatId(chatId);
      int messageId = callbackQuery.getMessage().getMessageId();

      log.info("New CallbackQuery from User:{}, userId:{}, with messageId:{} and data:{}",
          callbackQuery.getFrom().getFirstName() + " " + callbackQuery.getFrom().getLastName(),
          callbackQuery.getFrom().getId(), messageId, callbackQuery.getData());

      BotApiMethod<?> botResponse = null;
      try {
        botResponse = callbackProcessor.processCallbackQuery(callbackQuery);
      } catch (UnsupportedOperationException e) {
        log.error(e.getMessage(), e);
      }

      if (botResponse != null) {
        messageIdKeeper.setPreviousMessageId(messageId);
        messageIdKeeper.setNeedDeletePrevious(true);
        messageIdKeeper.setPreviousPreviousMessageId(messageId);
        messageIdKeeper.setNeedDeletePreviousPrevious(false);
        messageIdKeeper.setNeedDeleteImage(true);
        messageIdKeeperService.save(messageIdKeeper);
      }

      botResponseData = GenericBuilder.of(BotResponseData::new)
          .with(BotResponseData::setBotApiMethod, botResponse)
          .with(BotResponseData::setMessageIdKeeper, messageIdKeeper)
          .build();
    }

    if (message != null && message.hasText()) {
      long chatId = message.getChatId();
      MessageIdKeeper messageIdKeeper = messageIdKeeperService.getKeeperByChatId(chatId);
      messageIdKeeper.setPreviousMessageId(message.getMessageId());

      log.info("New message from User:{}, chatId:{}, messageId:{} with text: {}",
          message.getFrom().getFirstName() + " " + message.getFrom().getLastName(),
          chatId,
          message.getMessageId(),
          message.getText());

      SendMessage replyMessage = messageProcessor.handleInputMessage(message, messageIdKeeper);

      if (replyMessage != null) {
        messageIdKeeper.setNeedDeletePrevious(true);
        messageIdKeeper.setNeedDeletePreviousPrevious(true);
        messageIdKeeperService.save(messageIdKeeper);
      }

      botResponseData = GenericBuilder.of(BotResponseData::new)
          .with(BotResponseData::setBotApiMethod, replyMessage)
          .with(BotResponseData::setMessageIdKeeper, messageIdKeeper)
          .build();
    }

    return botResponseData;
  }

  public void saveMessageIdKeeper(MessageIdKeeper messageIdKeeper) {
    messageIdKeeperService.save(messageIdKeeper);
  }

  /*public void cleanImageMessageId(long chatId){
    messageIdKeeperService.cleanImageId(chatId);
  }

  public void cleanMessageIdsList(long chatId){
    messageIdKeeperService.cleanMessageIdsList(chatId);
  }*/
}
