package git.matheusoliveira04.api.fintrack.controller.exception;

import git.matheusoliveira04.api.fintrack.service.exception.ObjectNotFoundException;
import git.matheusoliveira04.api.fintrack.service.exception.UsernameNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<StandardError> getUsernameNotFoundException(UsernameNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new StandardError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), request.getRequestURI(), Collections.singletonList(exception.getMessage()))
        );
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> getObjectNotFoundException(ObjectNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new StandardError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), request.getRequestURI(), Collections.singletonList(exception.getMessage()))
        );
    }
}
