package org.neustupov.javadevinterviewbot.botapi.handlers.search;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.neustupov.javadevinterviewbot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchHandler implements InputMessageHandler {

  //TODO сообщения должен готовить ResponseMessageCreator вынести все туда
  ReplyMessageService replyMessageService;
  DataCache dataCache;

  public SearchHandler(
      ReplyMessageService replyMessageService,
      DataCache dataCache) {
    this.replyMessageService = replyMessageService;
    this.dataCache = dataCache;
  }

  @Override
  public SendMessage handle(Message message) {
    return processUsersInput(message);
  }

  private SendMessage processUsersInput(Message message) {
    long chatId = message.getChatId();
    SendMessage replyToUser = replyMessageService.getReplyMessage(chatId, "reply.search");
    dataCache.setUserCurrentBotState((int) chatId, BotState.FILLING_SEARCH);
    return replyToUser;
  }

  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_SEARCH;
  }
}
