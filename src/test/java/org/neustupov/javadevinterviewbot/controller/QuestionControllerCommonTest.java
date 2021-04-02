package org.neustupov.javadevinterviewbot.controller;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.neustupov.javadevinterviewbot.botapi.states.Category;
import org.neustupov.javadevinterviewbot.botapi.states.Level;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.service.QuestionNumService;
import org.neustupov.javadevinterviewbot.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class QuestionControllerCommonTest {

  static final String API = "/api";

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper mapper;

  @MockBean
  QuestionNumService questionNumService;

  @MockBean
  QuestionService questionService;

  Question q1;
  Question q2;

  Question question;

  @BeforeEach
  void setUp() {
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    Mockito.when(questionNumService.getNext())
        .thenReturn(1L)
        .thenReturn(2L);

    question = GenericBuilder.of(Question::new)
        .with(Question::setId, questionNumService.getNext())
        .with(Question::setCategory, Category.OOP)
        .with(Question::setLevel, Level.JUNIOR)
        .with(Question::setSmallDescription, "Тест2")
        .with(Question::setLargeDescription, "Тест2 тест")
        .build();

    q1 = GenericBuilder.of(Question::new)
        .with(Question::setId, 100L)
        .with(Question::setCategory, Category.OOP)
        .with(Question::setLevel, Level.JUNIOR)
        .with(Question::setSmallDescription, "SmallDescriptionOne")
        .with(Question::setLargeDescription, "LargeDescriptionOne")
        .build();

    q2 = GenericBuilder.of(Question::new)
        .with(Question::setId, 200L)
        .with(Question::setCategory, Category.COLLECTIONS)
        .with(Question::setLevel, Level.MIDDLE)
        .with(Question::setSmallDescription, "SmallDescriptionTwo")
        .with(Question::setLargeDescription, "LargeDescriptionTwo")
        .build();

    Mockito.when(questionService.findAll()).thenReturn(Collections.singletonList(question));
    Mockito.when(questionService.findById(any(Long.class))).thenReturn(Optional.of(question));
    Mockito.when(questionService.save(any(Question.class))).thenReturn(q1).thenReturn(q2);
  }
}
