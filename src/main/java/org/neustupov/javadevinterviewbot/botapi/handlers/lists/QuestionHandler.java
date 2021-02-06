package org.neustupov.javadevinterviewbot.botapi.handlers.lists;

import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.CategoryRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class QuestionHandler implements InputMessageHandler {

  private CategoryRepository categoryRepository;

  public QuestionHandler(
      CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public SendMessage handle(Message message) {
    long chatId = message.getChatId();
    Question question = categoryRepository.getQuestionByLink(message.getText());
    SendMessage sm = new SendMessage(chatId, question.getLargeDescription());
    sm.setReplyMarkup(getBackButton());
    return sm;
  }

  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_QUESTION;
  }
}
