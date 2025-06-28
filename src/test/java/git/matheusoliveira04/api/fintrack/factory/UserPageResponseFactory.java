package git.matheusoliveira04.api.fintrack.factory;

import git.matheusoliveira04.api.fintrack.dto.response.UserPageResponse;

public class UserPageResponseFactory {

    public static UserPageResponse build() {
        return UserPageResponse.builder()
                .users(UserResponseFactory.userResponseListBuild())
                .totalPages(1)
                .totalItems(2L)
                .build();
    }
}
