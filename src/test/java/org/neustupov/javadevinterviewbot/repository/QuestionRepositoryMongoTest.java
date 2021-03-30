package org.neustupov.javadevinterviewbot.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.neustupov.javadevinterviewbot.TestData.getListOfQuestion;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

@DataMongoTest
@ActiveProfiles("test")
class QuestionRepositoryMongoTest {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private QuestionRepositoryMongo questionRepositoryMongo;

  @BeforeEach
  void setUp() {
    List<Question> questionList = getListOfQuestion();
    questionList.forEach(question -> this.mongoTemplate.save(question));
  }

  @Test
  void getAllByCategoryAndLevel() {
    List<Question> resultList = questionRepositoryMongo.getAllByCategoryAndLevel(Category.COLLECTIONS, Level.JUNIOR);
    assertEquals(resultList.size(), 1);
    assertEquals(resultList.get(0).getSmallDescription(), "Test 2");
  }

  @Test
  void search() {
    List<Question> searchList = questionRepositoryMongo.search("1");
    assertEquals(searchList.size(), 1);
    assertEquals(searchList.get(0).getSmallDescription(), "Test 1");

    List<Question> resultList = questionRepositoryMongo.search("Test");
    assertEquals(resultList.size(), 2);
  }
}