package git.matheusoliveira04.api.fintrack.service.exception;

public class UsernameNotFoundException extends RuntimeException {

    public UsernameNotFoundException(String message) {
        super(message);
    }
}
