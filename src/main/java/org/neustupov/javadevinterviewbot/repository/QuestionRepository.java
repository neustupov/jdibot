package org.neustupov.javadevinterviewbot.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.neustupov.javadevinterviewbot.model.menu.Category;
import org.neustupov.javadevinterviewbot.model.menu.Level;
import org.neustupov.javadevinterviewbot.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<Question, Long> {

  List<Question> getAllByCategoryAndLevel(Category category, Level level);

  default List<Question> search(String searchString) {
    return findAll()
        .stream()
        .filter(q -> q.getSmallDescription().toLowerCase().contains(searchString.toLowerCase()))
        .collect(Collectors.toList());
  }
}
