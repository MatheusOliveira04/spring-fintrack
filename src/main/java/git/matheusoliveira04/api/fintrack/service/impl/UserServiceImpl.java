package git.matheusoliveira04.api.fintrack.service.impl;

import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.repository.UserRepository;
import git.matheusoliveira04.api.fintrack.service.UserService;
import git.matheusoliveira04.api.fintrack.service.exception.IntegrityViolationException;
import git.matheusoliveira04.api.fintrack.service.exception.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User insert(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        checkEmailIsUnique(user);
        return userRepository.save(user);
    }

    @Override
    public User findById(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User not found with id: " + id));
    }

    @Override
    public Page<User> findAll(int page, int size) {
        Page<User> usersFound = userRepository.findAll(PageRequest.of(page, size));
        if (usersFound.isEmpty()) {
            throw new ObjectNotFoundException("No user found!");
        }
        return usersFound;
    }

    @Override
    @Transactional
    public User update(User user) {
        findById(user.getId());
        checkEmailIsUnique(user);
        return userRepository.save(user);
    }

    private void checkEmailIsUnique(User user) {
        Optional<User> userFound = userRepository.findByEmail(user.getEmail());
        if (userFound.isPresent() && user.getId() != userFound.get().getId()) {
            throw new IntegrityViolationException("Email already exists!");
        }
    }
}
