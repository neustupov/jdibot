package org.neustupov.javadevinterviewbot.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepositoryMongo extends MongoRepository<Question, String> {

  List<Question> getAllByCategoryAndLevel(Category category, Level level);
  default List<Question> search(String searchString){
      return findAll()
          .stream()
          .filter(q -> q.getSmallDescription().toLowerCase().contains(searchString.toLowerCase()))
          .collect(Collectors.toList());
  }
}
