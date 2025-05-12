package git.matheusoliveira04.api.fintrack.service.exception;

public class IntegrityViolationException extends RuntimeException {

    public IntegrityViolationException(String message) {
        super(message);
    }
}
