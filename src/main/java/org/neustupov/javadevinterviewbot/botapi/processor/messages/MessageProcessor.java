package org.neustupov.javadevinterviewbot.botapi.processor.messages;

import static org.neustupov.javadevinterviewbot.botapi.processor.messages.MessageProcessor.Commands.QUESTION;
import static org.neustupov.javadevinterviewbot.botapi.processor.messages.MessageProcessor.Commands.START;

import java.io.File;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.neustupov.javadevinterviewbot.JavaDevInterviewBot;
import org.neustupov.javadevinterviewbot.botapi.BotStateContext;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.neustupov.javadevinterviewbot.model.MessageIdKeeper;
import org.neustupov.javadevinterviewbot.utils.ImageUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageProcessor {

  JavaDevInterviewBot bot;
  DataCache dataCache;
  BotStateContext botStateContext;
  ImageUtil imageUtil;

  interface Commands {

    String START = "/start";
    String QUESTION = "/q";
  }

  public MessageProcessor(DataCache dataCache,
      BotStateContext botStateContext, @Lazy JavaDevInterviewBot bot,
      ImageUtil imageUtil) {
    this.dataCache = dataCache;
    this.botStateContext = botStateContext;
    this.bot = bot;
    this.imageUtil = imageUtil;
  }

  public SendMessage handleInputMessage(Message message, MessageIdKeeper messageIdKeeper) {
    String inputMsg = message.getText();
    int userId = message.getFrom().getId();
    long chatId = message.getChatId();
    BotState botState;

    switch (inputMsg) {
      case START:
        botState = BotState.SHOW_START_MENU;
        dataCache.cleanAll(userId);
        dataCache.setUserCurrentBotState(userId, botState);
        break;
      default:
        botState = dataCache.getUserCurrentBotState(userId);
        break;
    }

    if (inputMsg.startsWith(QUESTION)) {
      botState = BotState.SHOW_QUESTION;
      dataCache.setUserCurrentBotState(userId, botState);
      Message responseImageMessage = processImage(inputMsg, chatId);
      if (messageIdKeeper != null && responseImageMessage != null) {
        messageIdKeeper.setImageMessageId(responseImageMessage.getMessageId());
        messageIdKeeper.setNeedDelete(false);
      }
      return botStateContext.processInputMessage(botState, message);
    }

    return botStateContext.processInputMessage(botState, message);
  }

  private Message processImage(String inputMsg, long chatId) {
    Optional<Binary> imageData = imageUtil.getImageData(inputMsg);
    Message message = null;
    if (imageData.isPresent()) {
      File imageTempFile = imageUtil.getPhotoFile(imageData.get());
      message = bot.sendPhoto(chatId, "", imageTempFile);
      imageUtil.deleteTempFile(imageTempFile);
    }
    return message;
  }
}
