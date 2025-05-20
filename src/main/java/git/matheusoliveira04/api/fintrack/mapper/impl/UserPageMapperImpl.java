package git.matheusoliveira04.api.fintrack.mapper.impl;

import git.matheusoliveira04.api.fintrack.dto.response.UserPageResponse;
import git.matheusoliveira04.api.fintrack.dto.response.UserResponse;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.mapper.UserPageMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserPageMapperImpl implements UserPageMapper {

    public UserPageResponse toUserPageResponse(List<UserResponse> usersResponse, Page<User> usersPage) {
        return UserPageResponse.builder()
                .users(usersResponse)
                .totalItems(usersPage.getTotalElements())
                .totalPages(usersPage.getTotalPages())
                .build();
    }

}
