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
import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.neustupov.javadevinterviewbot.model.MessageIdKeeper;
import org.neustupov.javadevinterviewbot.utils.ImageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
      BotStateContext botStateContext, JavaDevInterviewBot bot,
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
        botStateContext.processInputMessage(botState, message);
        break;
      default:
        botState = dataCache.getUserCurrentBotState(userId);
        break;
    }

    if (inputMsg.startsWith(QUESTION)) {
      botState = BotState.SHOW_QUESTION;
      dataCache.setUserCurrentBotState(userId, botState);
      Message responseImageMessage = processImage(inputMsg, chatId);

      setIdsInKeeper(messageIdKeeper, responseImageMessage);

      return botStateContext.processInputMessage(botState, message);
    }

    return botStateContext.processInputMessage(botState, message);
  }

  private void setIdsInKeeper(MessageIdKeeper messageIdKeeper, Message responseImageMessage) {
    if (messageIdKeeper != null && responseImageMessage != null) {
      messageIdKeeper.setImageMessageId(responseImageMessage.getMessageId());
      messageIdKeeper.setNeedDeleteImage(false);
    }
  }

  private Message processImage(String inputMsg, long chatId) {
    Optional<Binary> imageData = imageUtil.getImageData(inputMsg);
    Message message = null;
    if (imageData.isPresent()) {
      File imageTempFile = imageUtil.getPhotoFile(imageData.get());
      message = sendPhoto(chatId, imageTempFile);
      imageUtil.deleteTempFile(imageTempFile);
    }
    return message;
  }

  private Message sendPhoto(Long chatId, File file) {
    Message message = null;
    if (file != null) {
      SendPhoto sendPhoto = SendPhoto.builder()
          .photo(new InputFile(file))
          .chatId(chatId.toString())
          .caption("")
          .build();
      try {
        message = bot.execute(sendPhoto);
      } catch (TelegramApiException e) {
        log.error(e.getMessage(), e);
      }
    }
    return message;
  }
}
