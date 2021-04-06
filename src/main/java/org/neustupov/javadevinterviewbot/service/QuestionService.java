package org.neustupov.javadevinterviewbot.service;

import java.util.List;
import java.util.Optional;
import org.neustupov.javadevinterviewbot.model.Question;

/**
 * Сервис вопросов
 */
public interface QuestionService {

  /**
   * Сохраняет вопрос
   *
   * @param question Вопрос
   * @return Сохраненный вопрос
   */
  Question save(Question question);

  /**
   * Сохраняет список вопросов
   *
   * @param qList Список вопросов
   */
  void saveAll(List<Question> qList);

  /**
   * Найти все вопросы
   *
   * @return Список вопросов
   */
  List<Question> findAll();

  /**
   * Найти вопрос по Id
   *
   * @param id Id вопроса
   * @return Optional<Question>
   */
  Optional<Question> findById(Long id);

  /**
   * Удалить вопрос
   *
   * @param question Вопрос
   */
  void delete(Question question);

  /**
   * Удалить все вопросы
   */
  void deleteAll();
}
