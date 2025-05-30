package git.matheusoliveira04.api.fintrack.mapper;

import git.matheusoliveira04.api.fintrack.dto.request.UserRequest;
import git.matheusoliveira04.api.fintrack.dto.response.UserResponse;
import git.matheusoliveira04.api.fintrack.entity.Role;
import git.matheusoliveira04.api.fintrack.entity.User;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "entries", ignore = true)
    @Mapping(target = "categories", ignore = true)
    User toUser(UserRequest userRequest, @Context Set<Role> roles);

    @Mapping(target = "roleNames", ignore = true)
    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponses(List<User> users);

    @AfterMapping
    default void mapToUser(@MappingTarget User user, UserRequest userRequest, @Context Set<Role> roles) {
        user.addAllRoles(roles);
    }

    @AfterMapping
    default void mapToUserResponse(@MappingTarget UserResponse userResponse, User user) {
        user.getRoles().forEach(role -> userResponse.getRoleNames().add(role.getName().toString()));
    }
}
