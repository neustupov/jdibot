package org.neustupov.javadevinterviewbot.botapi.handlers.lists;

import static org.neustupov.javadevinterviewbot.botapi.states.BotState.SHOW_QUESTION;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.QuestionRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionHandler implements InputMessageHandler {

  //TODO сообщения должен готовить ResponseMessageCreator вынести все туда
  QuestionRepository questionRepository;
  ButtonMaker buttonMaker;

  public QuestionHandler(
      QuestionRepository questionRepository,
      ButtonMaker buttonMaker) {
    this.questionRepository = questionRepository;
    this.buttonMaker = buttonMaker;
  }

  @Override
  public SendMessage handle(Message message) {
    Long chatId = message.getChatId();
    Long id = getIdFromMessage(message);
    Optional<Question> questionOptional = questionRepository.findById(id);
    SendMessage sm = null;
    if (questionOptional.isPresent()) {
      sm = SendMessage.builder().chatId(chatId.toString())
          .text(questionOptional.get().getLargeDescription()).build();
      sm.setReplyMarkup(buttonMaker.getBackToQuestionsButton());
    }
    return sm;
  }

  private Long getIdFromMessage(Message message){
    return Long.parseLong(message.getText().replace("/q", ""));
  }

  @Override
  public BotState getHandlerName() {
    return SHOW_QUESTION;
  }
}
