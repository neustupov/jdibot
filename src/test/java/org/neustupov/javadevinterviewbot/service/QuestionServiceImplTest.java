package org.neustupov.javadevinterviewbot.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.neustupov.javadevinterviewbot.TestData.getListOfQuestion;
import static org.neustupov.javadevinterviewbot.TestData.getQuestion;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuestionServiceImplTest {

  @Autowired
  private QuestionServiceImpl questionService;

  @MockBean
  private QuestionNumService service;

  private Question question;
  private List<Question> questionList;

  @BeforeEach
  void setUp() {
    question = getQuestion();
    questionList = getListOfQuestion();

    when(service.getNext()).thenReturn(100500L);

    questionService.deleteAll();
  }

  @Test
  void save() {
    Question result = questionService.save(question);
    assertEquals(result.getSmallDescription(), "Test 3");
  }

  @Test
  void saveAll() {
    questionService.saveAll(questionList);
    assertEquals(questionService.findAll().size(), 2);
  }

  @Test
  void findAll() {
    assertEquals(questionService.findAll().size(), 0);

    questionService.saveAll(questionList);
    assertEquals(questionService.findAll().size(), 2);
  }

  @Test
  void findById() {
    questionService.save(question);
    Optional<Question> questionOptional = questionService.findById(100500L);
    assertTrue(questionOptional.isPresent());
    assertEquals(questionOptional.get().getSmallDescription(), "Test 3");
  }

  @Test
  void delete() {
    questionService.save(question);
    Optional<Question> questionOptional = questionService.findById(100500L);
    assertTrue(questionOptional.isPresent());

    questionService.delete(question);
    assertEquals(questionService.findAll().size(), 0);
  }

  @Test
  void deleteAll() {
    questionService.save(question);
    Optional<Question> questionOptional = questionService.findById(100500L);
    assertTrue(questionOptional.isPresent());

    questionService.deleteAll();
    assertEquals(questionService.findAll().size(), 0);
  }
}