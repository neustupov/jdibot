package org.neustupov.javadevinterviewbot.botapi.handlers.lists;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.CommonQuestionRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionHandler implements InputMessageHandler {

  //TODO сообщения должен готовить ResponseMessageCreator вынести все туда
  CommonQuestionRepository commonQuestionRepository;
  ButtonMaker buttonMaker;

  public QuestionHandler(
      CommonQuestionRepository commonQuestionRepository,
      ButtonMaker buttonMaker) {
    this.commonQuestionRepository = commonQuestionRepository;
    this.buttonMaker = buttonMaker;
  }

  @Override
  public SendMessage handle(Message message) {
    long chatId = message.getChatId();
    Question question = commonQuestionRepository.findByLink(message.getText());
    SendMessage sm = new SendMessage(chatId, question.getLargeDescription());
    sm.setReplyMarkup(buttonMaker.getBackButton());
    return sm;
  }

  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_QUESTION;
  }
}
