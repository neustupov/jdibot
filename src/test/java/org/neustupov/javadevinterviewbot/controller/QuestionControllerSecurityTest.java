package org.neustupov.javadevinterviewbot.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class QuestionControllerSecurityTest extends QuestionControllerCommonTest{

  @Test
  void initUnauthorizedUser() throws Exception {
    this.mockMvc
        .perform(get(API + "/init")
            .with(httpBasic("user", "user")))
        .andExpect(status().isUnauthorized())
        .andDo(print());
  }

  @Test
  void initAuthorizedUser() throws Exception {
    this.mockMvc
        .perform(get(API + "/init")
            .with(httpBasic("test", "test")))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  void getQuestionUnauthorizedUser() throws Exception {
    this.mockMvc
        .perform(get(API + "/question")
            .with(httpBasic("user", "user")))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void getQuestionAuthorizedUser() throws Exception {
    this.mockMvc
        .perform(get(API + "/question")
            .with(httpBasic("test", "test")))
        .andExpect(status().isOk());
  }

  @Test
  void getQuestionByIdUnauthorizedUser() throws Exception {
    this.mockMvc
        .perform(get(API + "/question/1")
            .with(httpBasic("user", "user")))
        .andExpect(status().isUnauthorized())
        .andDo(print());
  }

  @Test
  void getQuestionByIdAuthorizedUser() throws Exception {
    this.mockMvc
        .perform(get(API + "/question/111")
            .with(httpBasic("test", "test")))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  void createQuestionUnauthorizedUser() throws Exception {
    question.setId(5L);
    this.mockMvc
        .perform(post(API + "/question")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(question))
            .with(httpBasic("user", "user")))
        .andExpect(status().isUnauthorized())
        .andDo(print());
  }

  @Test
  void createQuestionAuthorizedUser() throws Exception {
    question.setId(5L);
    this.mockMvc
        .perform(post(API + "/question")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(question))
            .with(httpBasic("test", "test")))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  @Test
  void createQuestionsUnauthorizedUser() throws Exception {
    this.mockMvc
        .perform(post(API + "/questions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(Arrays.asList(q1, q2)))
            .with(httpBasic("user", "user")))
        .andExpect(status().isUnauthorized())
        .andDo(print());
  }

  @Test
  void createQuestionsAuthorizedUser() throws Exception {
    this.mockMvc
        .perform(post(API + "/questions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(Arrays.asList(q1, q2)))
            .with(httpBasic("test", "test")))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  void addImageToQuestionUnauthorizedUser() throws Exception {
    MockMultipartFile imageFile = new MockMultipartFile("image", "", "multipart/form-data",
        "image".getBytes());
    this.mockMvc
        .perform(multipart(API + "/question/1/image")
            .file(imageFile)
            .with(httpBasic("user", "user")))
        .andExpect(status().isUnauthorized())
        .andDo(print());
  }

  @Test
  void addImageToQuestionAuthorizedUser() throws Exception {
    MockMultipartFile imageFile = new MockMultipartFile("image", "", "multipart/form-data",
        "image".getBytes());
    this.mockMvc
        .perform(multipart(API + "/question/1/image")
            .file(imageFile)
            .with(httpBasic("test", "test")))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  void deleteQuestionUnauthorizedUser() throws Exception {
    this.mockMvc
        .perform(delete(API + "/question/1")
            .with(httpBasic("user", "user")))
        .andExpect(status().isUnauthorized())
        .andDo(print());
  }

  @Test
  void deleteQuestionAuthorizedUser() throws Exception {
    this.mockMvc
        .perform(delete(API + "/question/1")
            .with(httpBasic("test", "test")))
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void deleteQuestionWithObjectUnauthorizedUser() throws Exception {
    question.setId(1L);
    this.mockMvc
        .perform(delete(API + "/question")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(question))
            .with(httpBasic("user", "user")))
        .andExpect(status().isUnauthorized())
        .andDo(print());
  }

  @Test
  void deleteQuestionWithObjectAuthorizedUser() throws Exception {
    question.setId(1L);
    this.mockMvc
        .perform(delete(API + "/question")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(question))
            .with(httpBasic("test", "test")))
        .andExpect(status().isNoContent())
        .andDo(print());
  }
}
