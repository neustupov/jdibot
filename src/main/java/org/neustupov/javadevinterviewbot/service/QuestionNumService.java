package org.neustupov.javadevinterviewbot.service;

/**
 * Сервис объектов нумерации вопросов
 */
public interface QuestionNumService {

  /**
   * Следующее значение
   *
   * @return long
   */
  long getNext();

  /**
   * Инициализация
   */
  void init();
}
