package org.neustupov.javadevinterviewbot.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Collections;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.model.QuestionNum;
import org.neustupov.javadevinterviewbot.repository.QuestionNumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuestionNumServiceImplTest {

  @Autowired
  private QuestionNumServiceImpl questionNumService;

  @MockBean
  private QuestionNumRepository questionNumRepository;

  @BeforeEach
  void setUp() {
    QuestionNum last =new QuestionNum(400L);
    last.setId(new ObjectId());
    when(questionNumRepository.findTopByOrderByIdDesc()).thenReturn(last);
    when(questionNumRepository.findAll()).thenReturn(Collections.singletonList(new QuestionNum(100)));
  }

  @Test
  void getNext() {
    assertEquals(questionNumService.getNext(), 401L);
  }

  @Test
  void init() {
    questionNumService.init();
    assertEquals(questionNumRepository.findAll().size(), 1);
  }
}