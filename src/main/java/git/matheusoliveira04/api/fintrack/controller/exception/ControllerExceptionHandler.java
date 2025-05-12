package git.matheusoliveira04.api.fintrack.controller.exception;

import git.matheusoliveira04.api.fintrack.service.exception.IntegrityViolationException;
import git.matheusoliveira04.api.fintrack.service.exception.ObjectNotFoundException;
import git.matheusoliveira04.api.fintrack.service.exception.UsernameNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<StandardError> getUsernameNotFoundException(UsernameNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                new StandardError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), request.getRequestURI(), Collections.singletonList(exception.getMessage()))
        );
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> getObjectNotFoundException(ObjectNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                new StandardError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), request.getRequestURI(), Collections.singletonList(exception.getMessage()))
        );
    }

    @ExceptionHandler(IntegrityViolationException.class)
    public ResponseEntity<StandardError> getIntegrityViolationException(IntegrityViolationException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new StandardError(LocalDateTime.now(), HttpStatus.CONFLICT.value(), request.getRequestURI(), Collections.singletonList(exception.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> getMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        var allErrors = exception.getAllErrors()
                .stream()
                .map(errorField -> extractErrorMessage(((FieldError) errorField).getField(), errorField.getDefaultMessage()))
                .toList();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new StandardError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), request.getRequestURI(), allErrors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StandardError> getConstraintViolationException(ConstraintViolationException exception, HttpServletRequest request) {
        var allErrors = exception.getConstraintViolations()
                .stream()
                .map(error -> extractErrorMessage(error.getPropertyPath().toString(), error.getMessage()))
                .toList();
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new StandardError(LocalDateTime.now(), HttpStatus.CONFLICT.value(), request.getRequestURI(), allErrors));
    }

    private String extractErrorMessage(String fieldName, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(fieldName)
                .append(": ")
                .append(message);
        return sb.toString();
    }
}
