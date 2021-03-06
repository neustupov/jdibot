package org.neustupov.javadevinterviewbot.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.neustupov.javadevinterviewbot.QuestionAggregator;
import org.neustupov.javadevinterviewbot.TestUtil;
import org.neustupov.javadevinterviewbot.model.Question;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

@WithMockUser
class QuestionControllerTest extends QuestionControllerCommonTest{

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
  void createQuestion() throws Exception {
    question.setId(5L);
    this.mockMvc
        .perform(post(API + "/question")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(question)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("location"))
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
  void invalidQuestionSave(@AggregateWith(QuestionAggregator.class) Question question)
      throws Exception {
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