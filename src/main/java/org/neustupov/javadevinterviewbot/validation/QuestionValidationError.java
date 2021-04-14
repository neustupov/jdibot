package org.neustupov.javadevinterviewbot.validation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Ошибки валидации
 */
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public class QuestionValidationError {

  /**
   * Список ошибок
   */
  @Getter
  @JsonInclude(Include.NON_EMPTY)
  List<String> errors = new ArrayList<>();

  /**
   * Текст ошибки
   */
  @Getter
  String errorMessage;

  public QuestionValidationError(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  /**
   * Добавляет ошибки в список
   *
   * @param error Ошибка
   */
  void addValidationError(String error) {
    errors.add(error);
  }
}
