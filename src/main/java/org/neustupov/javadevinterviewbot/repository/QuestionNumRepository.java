package org.neustupov.javadevinterviewbot.repository;

import org.neustupov.javadevinterviewbot.model.QuestionNum;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionNumRepository extends MongoRepository<QuestionNum, String> {

  QuestionNum findTopByOrderByIdDesc();

}
