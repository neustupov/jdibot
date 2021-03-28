package org.neustupov.javadevinterviewbot.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.validation.QuestionValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuestionControllerErrorsTest {

  @Autowired
  private QuestionController questionController;

  @Test
  void handleException() {
    Exception exceptionForHandle = new RuntimeException("Exception for handling");
    QuestionValidationError validationError = questionController.handleException(exceptionForHandle);
    assertEquals(validationError.getErrorMessage(), exceptionForHandle.getMessage());
  }
}
