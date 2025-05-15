package git.matheusoliveira04.api.fintrack.mapper;

import git.matheusoliveira04.api.fintrack.dto.response.UserPageResponse;
import git.matheusoliveira04.api.fintrack.dto.response.UserResponse;

import java.util.List;

public interface UserPageMapper {
    UserPageResponse toUserPageResponse(List<UserResponse> usersResponse, Long totalItems, Integer totalPages);
}
