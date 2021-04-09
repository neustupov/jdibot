package org.neustupov.javadevinterviewbot.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bson.types.Binary;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.QuestionTempData;
import org.neustupov.javadevinterviewbot.service.QuestionService;
import org.neustupov.javadevinterviewbot.validation.QuestionValidationError;
import org.neustupov.javadevinterviewbot.validation.QuestionValidationErrorBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Контроллер вопросов
 */
@RestController
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionController {

  /**
   * Сервис вопросов
   */
  QuestionService questionService;

  /**
   * Данные для предварительного запролнения репозитория
   */
  QuestionTempData questionTempData;

  public QuestionController(QuestionService questionService,
      QuestionTempData questionTempData) {
    this.questionService = questionService;
    this.questionTempData = questionTempData;
  }

  /**
   * Заполняет репозиторий временными данными
   *
   * @return ResponseEntity
   */
  @GetMapping("/init")
  public ResponseEntity<Iterable<Question>> init() {
    questionTempData.initSeq();
    questionTempData.init();
    List<Question> qList = questionTempData.getQList();
    questionService.saveAll(qList);
    return ResponseEntity.ok(questionService.findAll());
  }

  /**
   * Получить все вопросы
   *
   * @return Список вопросов
   */
  @GetMapping("/question")
  public ResponseEntity<Iterable<Question>> getQuestion() {
    return ResponseEntity.ok(questionService.findAll());
  }

  /**
   * Получить вопрос по Id
   *
   * @param id Id вопроса
   * @return Вопрос
   */
  @GetMapping("/question/{id}")
  public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
    Optional<Question> questionOptional = questionService.findById(id);
    return questionOptional.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Создать вопрос
   *
   * @param question Вопрос
   * @param errors Ошибки
   * @return Урл для доступа к созданному вопросу в хидере ответа
   */
  @RequestMapping(value = "/question", method = {RequestMethod.POST, RequestMethod.PUT})
  public ResponseEntity<?> createQuestion(@Valid @RequestBody Question question, Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest()
          .body(QuestionValidationErrorBuilder.fromBindingsErrors(errors));
    }
    Question result = questionService.save(question);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(result.getId()).toUri();
    return ResponseEntity.created(location).build();
  }

  /**
   * Создает вопросы из списка
   *
   * @param question Список вопросов
   * @param errors Ошибки
   * @return Id созданных вопросов
   */
  @RequestMapping(value = "/questions", method = {RequestMethod.POST, RequestMethod.PUT})
  public ResponseEntity<?> createQuestions(@Valid @RequestBody List<Question> question,
      Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest()
          .body(QuestionValidationErrorBuilder.fromBindingsErrors(errors));
    }
    List<Long> questionIds = question.stream().map(q -> questionService.save(q).getId())
        .collect(Collectors.toList());
    return ResponseEntity.ok(questionIds);
  }

  /**
   * Добавляет изображение к вопросу
   *
   * @param id Id вопроса
   * @param multipartFile Изображение
   * @return ResponseEntity
   * @throws IOException IOException
   */
  @PostMapping(value = "/question/{id}/image")
  public ResponseEntity<?> addImageToQuestion(@PathVariable Long id,
      @RequestParam("image") MultipartFile multipartFile) throws IOException {
    Optional<Question> questionOptional = questionService.findById(id);
    if (questionOptional.isPresent()) {
      Question question = questionOptional.get();
      question.setImage(new Binary(multipartFile.getBytes()));
      questionService.save(question);
      return ResponseEntity.ok(questionService.findById(id));
    }
    return ResponseEntity.notFound().build();
  }

  /**
   * Удаляет вопрос по Id
   *
   * @param id Id вопроса
   * @return ResponseEntity
   */
  @DeleteMapping("/question/{id}")
  public ResponseEntity<Question> deleteQuestion(@PathVariable String id) {
    questionService
        .delete(GenericBuilder.of(Question::new).with(Question::setId, Long.parseLong(id)).build());
    return ResponseEntity.noContent().build();
  }

  /**
   * Удаляет вопрос
   *
   * @param question Вопрос
   * @return ResponseEntity
   */
  @DeleteMapping("/question")
  public ResponseEntity<Question> deleteQuestion(@RequestBody Question question) {
    questionService.delete(question);
    return ResponseEntity.noContent().build();
  }

  /**
   * Хендлер ошибок
   *
   * @param exception Ошибка
   * @return Ошибка валидации
   */
  @ExceptionHandler
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public QuestionValidationError handleException(Exception exception) {
    return new QuestionValidationError(exception.getMessage());
  }
}

