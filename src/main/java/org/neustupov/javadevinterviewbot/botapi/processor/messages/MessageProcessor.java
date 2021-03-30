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
import org.neustupov.javadevinterviewbot.service.ReplyMessageService;
import org.neustupov.javadevinterviewbot.utils.Emojis;
import org.neustupov.javadevinterviewbot.utils.ImageUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageProcessor {

  DataCache dataCache;
  BotStateContext botStateContext;
  @Lazy
  JavaDevInterviewBot bot;
  ReplyMessageService replyMessageService;
  ImageUtil imageUtil;

  interface Commands {

    String START = "/start";
    String QUESTION = "/q";
  }

  public MessageProcessor(DataCache dataCache,
      BotStateContext botStateContext, JavaDevInterviewBot bot,
      ReplyMessageService replyMessageService,
      ImageUtil imageUtil) {
    this.dataCache = dataCache;
    this.botStateContext = botStateContext;
    this.bot = bot;
    this.replyMessageService = replyMessageService;
    this.imageUtil = imageUtil;
  }

  public SendMessage handleInputMessage(Message message) {
    String inputMsg = message.getText();
    int userId = message.getFrom().getId();
    long chatId = message.getChatId();
    BotState botState;

    switch (inputMsg) {
      case START:
        botState = BotState.SHOW_START_MENU;
        dataCache.cleanAll(userId);
        dataCache.setUserCurrentBotState(userId, botState);
        bot.sendPhoto(chatId, replyMessageService.getReplyText("reply.pictureText", Emojis.WAVE),
            "static/images/bot-pic.jpg");
        break;
      default:
        botState = dataCache.getUserCurrentBotState(userId);
        break;
    }

    if (inputMsg.startsWith(QUESTION)) {
      botState = BotState.SHOW_QUESTION;
      dataCache.setUserCurrentBotState(userId, botState);
      processImage(inputMsg, chatId);
      return botStateContext.processInputMessage(botState, message);
    }

    return botStateContext.processInputMessage(botState, message);
  }

  private void processImage(String inputMsg, long chatId) {
    Optional<Binary> imageData = imageUtil.getImageData(inputMsg);
    imageData.ifPresent(binary -> {
      File imageTempFile = imageUtil.getPhotoFile(binary);
      bot.sendPhoto(chatId, "", imageTempFile);
      imageUtil.deleteTempFile(imageTempFile);
    });
  }
}
