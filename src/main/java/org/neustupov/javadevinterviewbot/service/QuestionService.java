package org.neustupov.javadevinterviewbot.service;

import java.util.List;
import java.util.Optional;
import org.neustupov.javadevinterviewbot.model.Question;

public interface QuestionService {

  Question save(Question question);
  void saveAll(List<Question> qList);
  List<Question> findAll();
  Optional<Question> findById(Long id);
  void delete(Question question);
  void deleteAll();
}
