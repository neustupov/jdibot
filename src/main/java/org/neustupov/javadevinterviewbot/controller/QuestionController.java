package org.neustupov.javadevinterviewbot.controller;

import java.net.URI;
import javax.validation.Valid;
import org.neustupov.javadevinterviewbot.model.Question;
import org.neustupov.javadevinterviewbot.repository.QuestionRepository;
import org.neustupov.javadevinterviewbot.validation.QuestionValidationError;
import org.neustupov.javadevinterviewbot.validation.QuestionValidationErrorBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api")
public class QuestionController {

  private QuestionRepository repository;

  public QuestionController(
      QuestionRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/question")
  public ResponseEntity<Iterable<Question>> getQuestion() {
    return ResponseEntity.ok(repository.findAll());
  }

  @GetMapping("/question/{link}")
  public ResponseEntity<Question> getQuestionByLink(@PathVariable String link) {
    return ResponseEntity.ok(repository.findByLink(link));
  }

  @RequestMapping(value = "/question", method = {RequestMethod.POST, RequestMethod.PUT})
  public ResponseEntity<?> createQuestion(@Valid @RequestBody Question question, Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest()
          .body(QuestionValidationErrorBuilder.fromBindingsErrors(errors));
    }
    Question result = repository.save(question);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{link}")
        .buildAndExpand(result.getLink()).toUri();
    return ResponseEntity.created(location).build();
  }

  @DeleteMapping("/question/{link}")
  public ResponseEntity<Question> deletQuestion(@PathVariable String link) {
    repository.delete(Question.builder().link(link).build());
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

