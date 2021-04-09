package org.neustupov.javadevinterviewbot.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

/**
 * Билдер объектов ошибок валидации вопросов
 */
public class QuestionValidationErrorBuilder {

  /**
   * Создает объект QuestionValidationError
   *
   * @param errors Ошибки
   * @return QuestionValidationError
   */
  public static QuestionValidationError fromBindingsErrors(Errors errors) {
    QuestionValidationError error = new QuestionValidationError("Validation failed. " +
        errors.getErrorCount() + " error(s)");
    for (ObjectError objectError : errors.getAllErrors()) {
      error.addValidationError(objectError.getDefaultMessage());
    }
    return error;
  }

}
