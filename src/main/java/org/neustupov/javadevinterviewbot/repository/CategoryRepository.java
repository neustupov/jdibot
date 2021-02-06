package org.neustupov.javadevinterviewbot.repository;

import java.util.List;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.model.Question;

public interface CategoryRepository {

  List<Question> getAllQuestionsByCategory(Category category);
  Question getQuestionByLink(String link);
}
