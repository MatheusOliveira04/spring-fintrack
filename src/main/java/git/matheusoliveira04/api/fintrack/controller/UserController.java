package git.matheusoliveira04.api.fintrack.controller;

import git.matheusoliveira04.api.fintrack.dto.request.UserRequest;
import git.matheusoliveira04.api.fintrack.dto.response.UserPageResponse;
import git.matheusoliveira04.api.fintrack.dto.response.UserResponse;
import git.matheusoliveira04.api.fintrack.entity.Role;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.mapper.UserMapper;
import git.matheusoliveira04.api.fintrack.mapper.UserPageMapper;
import git.matheusoliveira04.api.fintrack.repository.RoleRepository;
import git.matheusoliveira04.api.fintrack.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
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
    private BCryptPasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    private UserPageMapper userPageMapper;

    public UserController(RoleRepository roleRepository, UserService userService, BCryptPasswordEncoder passwordEncoder, UserMapper userMapper, UserPageMapper userPageMapper) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userPageMapper = userPageMapper;
    }

    @PostMapping
    public ResponseEntity<UserResponse> insert(@RequestBody @Valid @NotNull UserRequest userRequest, UriComponentsBuilder uriBuilder) {
        Set<Role> roles = new HashSet<>(roleRepository.findByNameIn(userRequest.getRoleName().stream().toList()));
        User user = userService.insert(userMapper.toUser(userRequest, roles));
        return ResponseEntity
                .created(uriBuilder.path("/api/v1/user").buildAndExpand(user.getId()).toUri())
                .body(userMapper.toUserResponse(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable @NotNull @NotBlank String id) {
        User user = userService.findById(UUID.fromString(id));
        return ResponseEntity.ok(userMapper.toUserResponse(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<UserPageResponse> findAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int size) {
        Page<User> users = userService.findAll(page, size);
        List<UserResponse> usersResponse = users.map(user -> userMapper.toUserResponse(user)).toList();
        UserPageResponse userPageResponse = userPageMapper.toUserPageResponse(usersResponse, users.getTotalElements(),
                users.getTotalPages());

        return ResponseEntity.ok(userPageResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable @NotNull @NotBlank String id, @RequestBody UserRequest userRequest) {
        Set<Role> roles = new HashSet<>(roleRepository.findByNameIn(userRequest.getRoleName().stream().toList()));
        var user = userMapper.toUser(userRequest, roles);
        user.setId(UUID.fromString(id));
        return ResponseEntity.ok(userMapper.toUserResponse(userService.update(user)));
    }
}