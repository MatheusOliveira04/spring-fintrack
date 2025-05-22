package git.matheusoliveira04.api.fintrack.util;

import git.matheusoliveira04.api.fintrack.config.jwts.JwtUtil;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.service.UserService;
import git.matheusoliveira04.api.fintrack.service.exception.IntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TokenUtil {

    private JwtUtil jwtUtil;
    private UserService userService;

    public TokenUtil(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    public UUID getUserIdByToken(String token) {
        return Optional.ofNullable(getUserByToken(token))
                .map(User::getId)
                .orElseThrow(() -> new IntegrityViolationException("The user id does not exist"));
    }

    public User getUserByToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Invalid token: cannot be empty or null!");
        }
        return userService.findByEmail(extractAuthorizationToken(token));
    }

    private String extractAuthorizationToken(String token) {
        return jwtUtil.extractUsername(extractBearerCharacters(token));
    }

    private String extractBearerCharacters(String token) {
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }
}
