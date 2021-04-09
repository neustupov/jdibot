package org.neustupov.javadevinterviewbot.repository;

import org.neustupov.javadevinterviewbot.model.QuestionNum;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Репозиторий QuestionNum
 */
public interface QuestionNumRepository extends MongoRepository<QuestionNum, String> {

  /**
   * Найти самый большой сиквенс
   *
   * @return QuestionNum
   */
  QuestionNum findTopByOrderByIdDesc();

}
