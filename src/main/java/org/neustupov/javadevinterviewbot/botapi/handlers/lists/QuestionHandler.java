package org.neustupov.javadevinterviewbot.botapi.handlers.lists;

import static org.neustupov.javadevinterviewbot.botapi.buttons.ButtonMaker.Callbacks.BACK_BUTTON;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.botapi.handlers.InputMessageHandler;
import org.neustupov.javadevinterviewbot.botapi.messagecreator.ResponseMessageCreator;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.QuestionRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Хэндлер отображения вопроса
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionHandler implements InputMessageHandler {

  /**
   * Класс, собирающий сообщение
   */
  ResponseMessageCreator responseMessageCreator;

  /**
   * Репозиторий вопросов
   */
  QuestionRepository questionRepository;

  public QuestionHandler(
      ResponseMessageCreator responseMessageCreator,
      QuestionRepository questionRepository) {
    this.responseMessageCreator = responseMessageCreator;
    this.questionRepository = questionRepository;
  }

  /**
   * Обрабатывает входящее сообщение
   *
   * @param message Входящее сообщение
   * @return SendMessage
   */
  @Override
  public SendMessage handle(Message message) {
    Optional<Question> questionOptional = questionRepository.findById(getIdFromMessage(message));
    return questionOptional.map(question -> responseMessageCreator
        .getSimplyMessage(message.getChatId(), question.getLargeDescription(),
            BotState.SHOW_QUESTION, BACK_BUTTON)).orElse(null);
  }

  /**
   * Извлекает Id вопроса из тела сообщения
   *
   * @param message Сообщение
   * @return Id вопроса
   */
  private Long getIdFromMessage(Message message) {
    return Long.parseLong(message.getText().replace("/q", ""));
  }

  /**
   * Возвращает сстояние, соответствующее хендлеру
   *
   * @return BotState
   */
  @Override
  public BotState getHandlerName() {
    return BotState.SHOW_QUESTION;
  }
}
