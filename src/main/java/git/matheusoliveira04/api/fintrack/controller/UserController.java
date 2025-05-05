package git.matheusoliveira04.api.fintrack.controller;

import git.matheusoliveira04.api.fintrack.dto.request.UserRequest;
import git.matheusoliveira04.api.fintrack.dto.response.UserResponse;
import git.matheusoliveira04.api.fintrack.entity.Role;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.mapper.UserMapper;
import git.matheusoliveira04.api.fintrack.repository.RoleRepository;
import git.matheusoliveira04.api.fintrack.repository.UserRepository;
import git.matheusoliveira04.api.fintrack.service.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @PostMapping
    @Transactional
    public ResponseEntity<UserResponse> insert(@RequestBody @NotNull UserRequest userRequest) {
        Set<Role> roles = new HashSet<>(roleRepository.findByNameIn(userRequest.getRoleName().stream().toList()));
        User user = userService.insert(userMapper.toUser(userRequest, roles));
        return ResponseEntity.ok(userMapper.toUserResponse(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable @NotBlank String id) {
        User user = userService.findById(UUID.fromString(id));
        return ResponseEntity.ok(userMapper.toUserResponse(user));
    }
}