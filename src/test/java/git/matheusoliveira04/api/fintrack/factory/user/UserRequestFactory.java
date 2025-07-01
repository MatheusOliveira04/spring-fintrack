package git.matheusoliveira04.api.fintrack.factory.user;

import git.matheusoliveira04.api.fintrack.dto.request.UserRequest;

import java.util.Set;

public class UserRequestFactory {

    public static UserRequest build() {
        return UserRequest.builder()
                .name("test 1")
                .email("test1@gmail.com")
                .password("123")
                .roleNames(Set.of("BASIC"))
                .build();
    }

    public static UserRequest mockUpdated() {
        return UserRequest.builder()
                .name("update 1")
                .email("update1@gmail.com")
                .password("update")
                .roleNames(Set.of("BASIC"))
                .build();
    }
}
