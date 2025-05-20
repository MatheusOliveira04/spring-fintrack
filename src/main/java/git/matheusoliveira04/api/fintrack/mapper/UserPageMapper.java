package git.matheusoliveira04.api.fintrack.mapper;

import git.matheusoliveira04.api.fintrack.dto.response.UserPageResponse;
import git.matheusoliveira04.api.fintrack.dto.response.UserResponse;
import git.matheusoliveira04.api.fintrack.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserPageMapper {
    UserPageResponse toUserPageResponse(List<UserResponse> usersResponse, Page<User> usersPage);
}
