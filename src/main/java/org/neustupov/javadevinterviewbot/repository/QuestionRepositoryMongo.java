package org.neustupov.javadevinterviewbot.repository;

import java.util.List;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepositoryMongo extends MongoRepository<Question, String>, QuestionRepository{

  List<Question> getAllByCategoryAndLevel(Category category, Level level);
  Question findByLink(String link);
  List<Question> search(String searchString);
}
