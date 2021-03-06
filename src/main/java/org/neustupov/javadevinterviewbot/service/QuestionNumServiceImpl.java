package org.neustupov.javadevinterviewbot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.model.QuestionNum;
import org.neustupov.javadevinterviewbot.repository.QuestionNumRepository;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса нумерации вопросов
 */
@Service
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public class QuestionNumServiceImpl implements QuestionNumService {

  /**
   * Репозиторий объектов нумерации вопросов
   */
  QuestionNumRepository questionNumRepository;

  public QuestionNumServiceImpl(
      QuestionNumRepository questionNumRepository) {
    this.questionNumRepository = questionNumRepository;
  }

  /**
   * Следующее значение
   *
   * @return long
   */
  @Override
  public long getNext() {
    QuestionNum last = questionNumRepository.findTopByOrderByIdDesc();
    long lastNum = last.seq;
    QuestionNum next = new QuestionNum(lastNum + 1);
    questionNumRepository.save(next);
    return next.seq;
  }

  /**
   * Инициализация
   */
  public void init() {
    questionNumRepository.save(new QuestionNum(100));
  }
}
