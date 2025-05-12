package git.matheusoliveira04.api.fintrack.mapper;

import git.matheusoliveira04.api.fintrack.dto.response.UserPageResponse;
import git.matheusoliveira04.api.fintrack.dto.response.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserPageMapper {

    public UserPageResponse toUserPageResponse(List<UserResponse> usersResponse, Long totalItems, Integer totalPages) {
        return UserPageResponse.builder()
                .users(usersResponse)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .build();
    }

}
