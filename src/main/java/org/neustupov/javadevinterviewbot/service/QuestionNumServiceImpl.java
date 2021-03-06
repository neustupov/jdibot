package org.neustupov.javadevinterviewbot.service;

import org.neustupov.javadevinterviewbot.model.QuestionNum;
import org.neustupov.javadevinterviewbot.repository.QuestionNumRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionNumServiceImpl implements QuestionNumService {

  private QuestionNumRepository questionNumRepository;

  public QuestionNumServiceImpl(
      QuestionNumRepository questionNumRepository) {
    this.questionNumRepository = questionNumRepository;
  }

  @Override
  public long getNext() {
    QuestionNum last = questionNumRepository.findTopByOrderByIdDesc();
    long lastNum = last.seq;
    QuestionNum next = new QuestionNum(lastNum + 1);
    questionNumRepository.save(next);
    return next.seq;
  }

  public void init(){
    questionNumRepository.save(new QuestionNum(100));
  }
}
