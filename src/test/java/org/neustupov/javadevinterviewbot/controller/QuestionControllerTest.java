package org.neustupov.javadevinterviewbot.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.neustupov.javadevinterviewbot.QuestionAggregator;
import org.neustupov.javadevinterviewbot.TestUtil;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class QuestionControllerTest {

  private static final String API = "/api";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private QuestionNumService questionNumService;

  @MockBean
  private QuestionService questionService;

  private Question q1;
  private Question q2;

  private Question question;

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

  @Test
  void init() throws Exception {
    this.mockMvc
        .perform(get(API + "/init"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].category").value("OOP"))
        .andDo(print());
  }

  @Test
  void getQuestion() throws Exception {
    this.mockMvc
        .perform(get(API + "/question"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].category").value("OOP"))
        .andDo(print());
  }

  @Test
  void getQuestionById() throws Exception {
    this.mockMvc
        .perform(get(API + "/question/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.category").value("OOP"))
        .andDo(print());
  }

  @Test
  void createQuestions() throws Exception {

    MvcResult mvcResult = this.mockMvc
        .perform(post(API + "/questions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(Arrays.asList(q1, q2))))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    List ids = (List) TestUtil
        .convertJSONStringToObject(mvcResult.getResponse().getContentAsString(), List.class);
    assertEquals(ids.size(), 2);
    assertNotEquals(ids.get(0), ids.get(1));
  }

  @Test
  void addImageToQuestion() throws Exception {
    MockMultipartFile imageFile = new MockMultipartFile("image", "", "multipart/form-data",
        "image".getBytes());
    this.mockMvc
        .perform(multipart(API + "/question/1/image")
            .file(imageFile))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andDo(print());
  }

  @Test
  void deleteQuestion() throws Exception {
    this.mockMvc
        .perform(delete(API + "/question/1"))
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void deleteQuestionWithObject() throws Exception {
    question.setId(1L);
    this.mockMvc
        .perform(delete(API + "/question")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(question)))
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @ParameterizedTest
  @CsvSource({
      "OOP, JUNIOR, Not Empty Small Description, ",
      "OOP, JUNIOR, , Not Empty Large Description",
      " , JUNIOR, Not Empty Small Description, Not Empty Large Description",
      "OOP, , Not Empty Small Description, Not Empty Large Description"
  })
  void invalidQuestionSave(@AggregateWith(QuestionAggregator.class) Question question) throws Exception {
    this.mockMvc
        .perform(post(API + "/question")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(question)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").isArray())
        .andExpect(jsonPath("$.errors").isNotEmpty())
        .andDo(print());
  }
}