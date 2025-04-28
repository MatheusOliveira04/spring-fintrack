package git.matheusoliveira04.api.fintrack.controller;

import git.matheusoliveira04.api.fintrack.dto.UserRequest;
import git.matheusoliveira04.api.fintrack.entity.Role;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.repository.RoleRepository;
import git.matheusoliveira04.api.fintrack.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private RoleRepository roleRepository;
    private UserRepository userRepository;

    public UserController(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<User> insert(@RequestBody UserRequest userRequest) {
        Set<Role> role = new HashSet<>(roleRepository.findByNameIn(userRequest.getRoleName().stream().toList()));
        User user = userRepository.save(
                new User(null, userRequest.getName(), userRequest.getUsername(), userRequest.getPassword(),
                        role)
        );
        return ResponseEntity.ok(user);
    }
}
