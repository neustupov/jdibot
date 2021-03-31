package org.neustupov.javadevinterviewbot.service;

import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.QuestionRepositoryMongo;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionServiceImpl implements QuestionService {

  QuestionRepositoryMongo repository;
  QuestionNumService service;

  public QuestionServiceImpl(
      QuestionRepositoryMongo repository,
      QuestionNumService service) {
    this.repository = repository;
    this.service = service;
  }

  @Override
  public Question save(Question question) {
    long questionId = question.getId();
    if (questionId == 0) {
      question.setId(service.getNext());
    }
    return repository.save(question);
  }

  @Override
  public void saveAll(List<Question> qList) {
    qList.forEach(this::save);
  }

  @Override
  public List<Question> findAll() {
    return repository.findAll();
  }

  @Override
  public Optional<Question> findById(Long id) {
    return repository.findById(id);
  }

  @Override
  public void delete(Question question) {
    repository.delete(question);
  }

  @Override
  public void deleteAll() {
    repository.deleteAll();
  }
}
