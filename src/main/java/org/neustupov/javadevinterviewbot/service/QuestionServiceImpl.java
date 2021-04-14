package org.neustupov.javadevinterviewbot.service;

import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.QuestionRepository;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса вопросов
 */
@Service
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public class QuestionServiceImpl implements QuestionService {

  /**
   * Репозиторий вопросов
   */
  QuestionRepository repository;

  /**
   * Сервис объектов нумерации вопросов
   */
  QuestionNumService service;

  public QuestionServiceImpl(
      QuestionRepository repository,
      QuestionNumService service) {
    this.repository = repository;
    this.service = service;
  }

  /**
   * Сохраняет вопрос
   *
   * @param question Вопрос
   * @return Сохраненный вопрос
   */
  @Override
  public Question save(Question question) {
    long questionId = question.getId();
    if (questionId == 0) {
      question.setId(service.getNext());
    }
    return repository.save(question);
  }

  /**
   * Сохраняет список вопросов
   *
   * @param qList Список вопросов
   */
  @Override
  public void saveAll(List<Question> qList) {
    qList.forEach(this::save);
  }

  /**
   * Найти все вопросы
   *
   * @return Список вопросов
   */
  @Override
  public List<Question> findAll() {
    return repository.findAll();
  }

  /**
   * Найти вопрос по Id
   *
   * @param id Id вопроса
   * @return Optional<Question>
   */
  @Override
  public Optional<Question> findById(Long id) {
    return repository.findById(id);
  }

  /**
   * Удалить вопрос
   *
   * @param question Вопрос
   */
  @Override
  public void delete(Question question) {
    repository.delete(question);
  }

  /**
   * Удалить все вопросы
   */
  @Override
  public void deleteAll() {
    repository.deleteAll();
  }
}
