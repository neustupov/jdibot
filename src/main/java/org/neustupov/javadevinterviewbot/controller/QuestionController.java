package org.neustupov.javadevinterviewbot.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bson.types.Binary;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.QuestionRepositoryMongo;
import org.neustupov.javadevinterviewbot.repository.QuestionTempData;
import org.neustupov.javadevinterviewbot.validation.QuestionValidationError;
import org.neustupov.javadevinterviewbot.validation.QuestionValidationErrorBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionController {

  QuestionRepositoryMongo repository;
  QuestionTempData questionTempData;

  public QuestionController(
      QuestionRepositoryMongo repository,
      QuestionTempData questionTempData) {
    this.repository = repository;
    this.questionTempData = questionTempData;
  }

  @GetMapping("/init")
  public ResponseEntity<Iterable<Question>> init() {
    questionTempData.initSeq();
    questionTempData.init();
    List<Question> qList = questionTempData.getQList();
    repository.saveAll(qList);
    return ResponseEntity.ok(repository.findAll());
  }

  @GetMapping("/question")
  public ResponseEntity<Iterable<Question>> getQuestion() {
    return ResponseEntity.ok(repository.findAll());
  }

  @GetMapping("/question/{id}")
  public ResponseEntity<Question> getQuestionById(@PathVariable String id) {
    Optional<Question> questionOptional = repository.findById(id);
    return questionOptional.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @RequestMapping(value = "/question", method = {RequestMethod.POST, RequestMethod.PUT})
  public ResponseEntity<?> createQuestion(@Valid @RequestBody Question question, Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest()
          .body(QuestionValidationErrorBuilder.fromBindingsErrors(errors));
    }
    Question result = repository.save(question);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(result.getId()).toUri();
    return ResponseEntity.created(location).build();
  }

  @PostMapping(value = "/question/{id}/image")
  public ResponseEntity<?> addImageToQuestion(@PathVariable String id,
      @RequestParam("image") MultipartFile multipartFile) throws IOException {
    Optional<Question> questionOptional = repository.findById(id);
    if (questionOptional.isPresent()) {
      Question question = questionOptional.get();
      question.setImage(new Binary(multipartFile.getBytes()));
      repository.save(question);
      return ResponseEntity.ok(repository.findById(id));
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/question/{id}")
  public ResponseEntity<Question> deleteQuestion(@PathVariable String id) {
    repository.delete(Question.builder().id(Long.parseLong(id)).build());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/question")
  public ResponseEntity<Question> deleteToDo(@RequestBody Question question) {
    repository.delete(question);
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public QuestionValidationError handleException(Exception exception) {
    return new QuestionValidationError(exception.getMessage());
  }
}

