package org.neustupov.javadevinterviewbot.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.neustupov.javadevinterviewbot.TestData.getListOfQuestion;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.model.menu.Category;
import org.neustupov.javadevinterviewbot.model.menu.Level;
import org.neustupov.javadevinterviewbot.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

@DataMongoTest
@ActiveProfiles("test")
class QuestionRepositoryTest {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private QuestionRepository questionRepository;

  @BeforeEach
  void setUp() {
    List<Question> questionList = getListOfQuestion();
    questionList.forEach(question -> this.mongoTemplate.save(question));
  }

  @Test
  void getAllByCategoryAndLevel() {
    List<Question> resultList = questionRepository
        .getAllByCategoryAndLevel(Category.COLLECTIONS, Level.JUNIOR);
    assertEquals(resultList.size(), 1);
    assertEquals(resultList.get(0).getSmallDescription(), "Test 2");
  }

  @Test
  void search() {
    List<Question> searchList = questionRepository.search("1");
    assertEquals(searchList.size(), 1);
    assertEquals(searchList.get(0).getSmallDescription(), "Test 1");

    List<Question> resultList = questionRepository.search("Test");
    assertEquals(resultList.size(), 2);
  }
}