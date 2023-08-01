package com.aubay.challenge.backend.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String MESSAGE = "message";

  @Override
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatusCode status, WebRequest request) {
    Map<String, List<String>> body = new HashMap<>();

    List<String> errors =
        ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();

    body.put(MESSAGE, errors);

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, String>> resourceNotFoundException(ResourceNotFoundException ex,
      WebRequest request) {
    Map<String, String> body = new HashMap<>();

    body.put(MESSAGE, ex.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Map<String, String>> userAlreadyExistsException(UserAlreadyExistsException ex,
      WebRequest request) {
    Map<String, String> body = new HashMap<>();

    body.put(MESSAGE, ex.getMessage());

    return ResponseEntity.badRequest().body(body);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, List<String>>> constraintViolationException(ConstraintViolationException ex,
      WebRequest request) {
    List<String> errors = new ArrayList<>();

    ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));

    Map<String, List<String>> body = new HashMap<>();
    body.put(MESSAGE, errors);

    return ResponseEntity.badRequest().body(body);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, String>> badCredentialsException(BadCredentialsException ex, WebRequest request) {
    Map<String, String> body = new HashMap<>();

    body.put(MESSAGE, ex.getMessage());

    return ResponseEntity.badRequest().body(body);
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Map<String, String>> accessDeniedExceptionn(AccessDeniedException ex, WebRequest request) {
    Map<String, String> body = new HashMap<>();

    body.put(MESSAGE, ex.getMessage());

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(InternalServerErrorException.class)
  public ResponseEntity<Map<String, String>> internalServerErrorExceptionn(InternalServerErrorException ex,
      WebRequest request) {
    Map<String, String> body = new HashMap<>();

    body.put(MESSAGE, ex.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }
}
