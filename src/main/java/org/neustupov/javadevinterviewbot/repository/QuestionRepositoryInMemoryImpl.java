package org.neustupov.javadevinterviewbot.repository;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.model.Question;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionRepositoryInMemoryImpl implements CommonQuestionRepository {

  private Map<String, Question> qMap;

  public QuestionRepositoryInMemoryImpl(
      TempData tempData) {
    this.qMap = tempData.getQList();
  }

  @Override
  public Question save(Question question) {
    Question result = qMap.get(question.getLink());
    if (result != null) {
      result.setLink(question.getLink());
      result.setLevel(question.getLevel());
      result.setCategory(question.getCategory());
      result.setSmallDescription(question.getSmallDescription());
      result.setLargeDescription(question.getLargeDescription());
      question = result;
    }
    qMap.put(question.getLink(), question);
    return qMap.get(question.getLink());
  }

  @Override
  public Iterable<Question> save(Collection<Question> question) {
    question.forEach(this::save);
    return findAll();
  }

  @Override
  public void delete(Question question) {
    qMap.remove(question.getLink());
  }

  @Override
  public Iterable<Question> findAll() {
    return qMap.entrySet()
        .stream()
        .sorted(entryComparator)
        .map(Map.Entry::getValue)
        .collect(Collectors.toList());
  }

  @Override
  public List<Question> getAllByCategoryAndLevel(Category category, Level level) {
    return qMap.entrySet()
        .stream()
        .map(Map.Entry::getValue)
        .filter(q -> q.getCategory().equals(category) && q.getLevel().equals(level))
        .collect(Collectors.toList());
  }

  @Override
  public Question findByLink(String link) {
    return qMap.entrySet()
        .stream()
        .map(Map.Entry::getValue)
        .filter(q -> q.getLink().equals(link))
        .findAny()
        .get();
  }

  @Override
  public List<Question> search(String searchString) {
    return qMap.entrySet()
        .stream()
        .map(Map.Entry::getValue)
        .filter(q -> q.getSmallDescription().toLowerCase().contains(searchString.toLowerCase()))
        .collect(Collectors.toList());
  }

  private Comparator<Entry<String, Question>> entryComparator =
      Comparator.comparing((Entry<String, Question> q) -> q.getValue().getLink());
}
