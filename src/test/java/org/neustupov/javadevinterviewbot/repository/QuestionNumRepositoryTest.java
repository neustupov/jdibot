package org.neustupov.javadevinterviewbot.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.QuestionNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataMongoTest
@ActiveProfiles("test")
class QuestionNumRepositoryTest {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private QuestionNumRepository questionNumRepository;

  @BeforeEach
  void setUp() {
    QuestionNum questionNumOne = new QuestionNum(300L);
    questionNumOne.setId(new ObjectId());

    QuestionNum questionNumTwo = new QuestionNum(200L);
    questionNumTwo.setId(new ObjectId());

    QuestionNum questionNumThree = new QuestionNum(100L);
    questionNumThree.setId(new ObjectId());

    mongoTemplate.save(questionNumOne);
    mongoTemplate.save(questionNumTwo);
    mongoTemplate.save(questionNumThree);
  }

  @Test
  void findTopByOrderByIdDesc() {
    QuestionNum questionNum = questionNumRepository.findTopByOrderByIdDesc();
    assertNotNull(questionNum);
    assertEquals(questionNum.getSeq(), 100L);
  }
}