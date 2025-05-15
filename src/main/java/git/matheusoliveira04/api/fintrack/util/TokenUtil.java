package git.matheusoliveira04.api.fintrack.util;

public class TokenUtil {

    public static String extractBearerCharacters(String token) {
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }
}
