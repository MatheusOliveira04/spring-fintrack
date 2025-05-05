package git.matheusoliveira04.api.fintrack.mapper;

import git.matheusoliveira04.api.fintrack.dto.request.UserRequest;
import git.matheusoliveira04.api.fintrack.dto.response.UserResponse;
import git.matheusoliveira04.api.fintrack.entity.Role;
import git.matheusoliveira04.api.fintrack.entity.User;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class UserMapper {

    public User toUser(UserRequest userRequest, @Context Set<Role> roles) {
        User user = toUser(userRequest);
        user.addAllRoles(roles);
        return user;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    protected abstract User toUser(UserRequest userRequest);

    public UserResponse toUserResponse(User user) {
        UserResponse userResponse = toUserResponseMapper(user);
        user.getRoles().forEach(role -> userResponse.getRoleNames().add(role.getName()));
        return userResponse;
    }

    @Mapping(target = "roleNames", ignore = true)
    protected abstract UserResponse toUserResponseMapper(User user);
}
