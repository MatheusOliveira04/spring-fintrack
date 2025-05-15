package git.matheusoliveira04.api.fintrack.mapper;

import git.matheusoliveira04.api.fintrack.dto.request.UserRequest;
import git.matheusoliveira04.api.fintrack.dto.response.UserResponse;
import git.matheusoliveira04.api.fintrack.entity.Role;
import git.matheusoliveira04.api.fintrack.entity.User;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "entries", ignore = true)
    @Mapping(target = "categories", ignore = true)
    User toUser(UserRequest userRequest);

    @Mapping(target = "roleNames", ignore = true)
    UserResponse toUserResponseMapper(User user);

    default User toUser(UserRequest userRequest, @Context Set<Role> roles) {
        User user = toUser(userRequest);
        user.addAllRoles(roles);
        return user;
    }

    default UserResponse toUserResponse(User user) {
        UserResponse userResponse = toUserResponseMapper(user);
        user.getRoles().forEach(role -> userResponse.getRoleNames().add(role.getName().toString()));
        return userResponse;
    }
}
