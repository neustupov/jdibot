package org.neustupov.javadevinterviewbot.repository;

import java.util.Collection;
import java.util.List;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.model.Question;

public interface CommonQuestionRepository {

  Question save(Question question);
  Iterable<Question> save(Collection<Question> question);
  void delete(Question question);
  Iterable<Question> findAll();

  List<Question> getAllByCategoryAndLevel(Category category, Level level);
  Question findByLink(String link);
  List<Question> search(String searchString);
}
