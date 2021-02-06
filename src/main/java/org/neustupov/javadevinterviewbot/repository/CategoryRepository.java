package org.neustupov.javadevinterviewbot.repository;

import java.util.List;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.model.Question;

public interface CategoryRepository {

  List<Question> getAllQuestionsByCategoryAndLevel(Category category, Level level);
  Question getQuestionByLink(String link);
  List<Question> search(String searchString);
}
