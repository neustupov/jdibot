package org.neustupov.javadevinterviewbot.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.neustupov.javadevinterviewbot.model.menu.Category;
import org.neustupov.javadevinterviewbot.model.menu.Level;
import org.neustupov.javadevinterviewbot.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Репозиторий Question
 */
public interface QuestionRepository extends MongoRepository<Question, Long> {

  /**
   * Найти все вопросы по категории и уровню
   *
   * @param category Категория
   * @param level Уровень
   * @return Список вопросов
   */
  List<Question> getAllByCategoryAndLevel(Category category, Level level);

  /**
   * Поиск по названию
   *
   * @param searchString Текст
   * @return Список вопросов
   */
  default List<Question> search(String searchString) {
    return findAll()
        .stream()
        .filter(q -> q.getSmallDescription().toLowerCase().contains(searchString.toLowerCase()))
        .collect(Collectors.toList());
  }
}
