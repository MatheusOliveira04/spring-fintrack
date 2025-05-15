package git.matheusoliveira04.api.fintrack.mapper.impl;

import git.matheusoliveira04.api.fintrack.dto.response.UserPageResponse;
import git.matheusoliveira04.api.fintrack.dto.response.UserResponse;
import git.matheusoliveira04.api.fintrack.mapper.UserPageMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserPageMapperImpl implements UserPageMapper {

    public UserPageResponse toUserPageResponse(List<UserResponse> usersResponse, Long totalItems, Integer totalPages) {
        return UserPageResponse.builder()
                .users(usersResponse)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .build();
    }

}
