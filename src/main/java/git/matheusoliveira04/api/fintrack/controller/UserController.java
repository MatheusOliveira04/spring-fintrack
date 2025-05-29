package git.matheusoliveira04.api.fintrack.controller;

import git.matheusoliveira04.api.fintrack.dto.request.UserRequest;
import git.matheusoliveira04.api.fintrack.dto.response.UserPageResponse;
import git.matheusoliveira04.api.fintrack.dto.response.UserResponse;
import git.matheusoliveira04.api.fintrack.entity.Role;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.entity.enums.RoleName;
import git.matheusoliveira04.api.fintrack.mapper.UserMapper;
import git.matheusoliveira04.api.fintrack.mapper.UserPageMapper;
import git.matheusoliveira04.api.fintrack.repository.RoleRepository;
import git.matheusoliveira04.api.fintrack.service.UserService;
import git.matheusoliveira04.api.fintrack.validation.annotation.ValidUUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private RoleRepository roleRepository;
    private UserService userService;
    private UserMapper userMapper;
    private UserPageMapper userPageMapper;

    public UserController(RoleRepository roleRepository, UserService userService, UserMapper userMapper, UserPageMapper userPageMapper) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.userMapper = userMapper;
        this.userPageMapper = userPageMapper;
    }

    @PostMapping
    public ResponseEntity<UserResponse> insert(
            @RequestBody @Valid @NotNull UserRequest userRequest,
            UriComponentsBuilder uriBuilder) {
        Set<Role> roles = getRoles(userRequest);
        User user = userService.insert(userMapper.toUser(userRequest, roles));
        return ResponseEntity
                .created(uriBuilder.path("/api/v1/user").buildAndExpand(user.getId()).toUri())
                .body(userMapper.toUserResponse(user));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable @NotNull @NotBlank @ValidUUID String id) {
        return ResponseEntity.ok(userMapper.toUserResponse(userService.findById(UUID.fromString(id))));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<UserPageResponse> findAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int size) {
        Page<User> users = userService.findAll(page, size);
        List<UserResponse> usersResponse = userMapper.toUserResponse(users.toList());
        UserPageResponse userPageResponse = userPageMapper.toUserPageResponse(usersResponse, users);
        return ResponseEntity.ok(userPageResponse);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable @NotBlank @ValidUUID String id,
            @RequestBody UserRequest userRequest) {
        Set<Role> roles = getRoles(userRequest);
        var user = userMapper.toUser(userRequest, roles);
        user.setId(UUID.fromString(id));
        return ResponseEntity.ok(userMapper.toUserResponse(userService.update(user)));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotBlank @ValidUUID String id) {
        userService.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    private HashSet<Role> getRoles(UserRequest userRequest) {
        return new HashSet<>(
                roleRepository.findByNameIn(userRequest.getRoleNames().stream().map(RoleName::valueOf).toList())
        );
    }
}