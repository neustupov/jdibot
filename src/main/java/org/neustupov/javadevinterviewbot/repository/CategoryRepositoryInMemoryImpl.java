package org.neustupov.javadevinterviewbot.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.model.Question;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepositoryInMemoryImpl implements CategoryRepository {

  private List<Question> qList = new ArrayList<>();

  {
    qList.add(Question.builder()
        .link("/q0000")
        .category(Category.OOP)
        .smallDescription("Что такое ООП")
        .largeDescription("Это очень крутая штука!")
        .build());
    qList.add(Question.builder()
        .link("/q0001")
        .category(Category.OOP)
        .smallDescription("Как с этим бороться?")
        .largeDescription("Никто этого не знает")
        .build());
  }

  @Override
  public List<Question> getAllQuestionsByCategory(Category category) {
    return qList.stream()
        .filter(q -> q.getCategory().equals(Category.OOP))
        .collect(Collectors.toList());
  }

  @Override
  public Question getQuestionByLink(String link) {
    return qList.stream().filter(q -> q.getLink().equals(link)).findAny().get();
  }
}
