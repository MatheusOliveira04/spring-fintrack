package git.matheusoliveira04.api.fintrack.factory;

import git.matheusoliveira04.api.fintrack.dto.response.UserResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class UserResponseFactory {

    public static UserResponse build() {
        return UserResponse.builder()
                .id(UUID.randomUUID().toString())
                .name("Test1")
                .email("Test1@gmail.com")
                .password("123")
                .roleNames(Set.of("ADMIN"))
                .build();
    }

    public static List<UserResponse> userResponseListBuild() {
        var userResponse = UserResponse.builder()
                .id(UUID.randomUUID().toString())
                .name("Test2")
                .email("Test2@gmail.com")
                .password("123")
                .roleNames(Set.of("ADMIN"))
                .build();
        return List.of(build(), userResponse);
    }
}
