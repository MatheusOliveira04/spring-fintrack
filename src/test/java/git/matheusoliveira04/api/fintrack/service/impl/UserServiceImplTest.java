package git.matheusoliveira04.api.fintrack.service.impl;

import git.matheusoliveira04.api.fintrack.entity.Role;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.entity.enums.RoleName;
import git.matheusoliveira04.api.fintrack.repository.UserRepository;
import git.matheusoliveira04.api.fintrack.service.exception.IntegrityViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> emailArgumentCaptor;

    @Nested
    class insert {

        @Test
        @DisplayName("Should insert a user with success")
        void shouldInsertUserSuccess() {
            var user = mockUser();
            var encodedPassword = "encodedPassword123";

            doReturn(encodedPassword).when(passwordEncoder).encode(user.getPassword());
            doReturn(Optional.empty()).when(repository).findByEmail(user.getEmail());
            doReturn(user).when(repository).save(userArgumentCaptor.capture());

            var output = service.insert(user);

            assertNotNull(output);
            assertEquals(user, output);

            var userCaptured = userArgumentCaptor.getValue();
            assertEquals(user.getId(), userCaptured.getId());
            assertEquals(user.getName(), userCaptured.getName());
            assertEquals(user.getEmail(), userCaptured.getEmail());
            assertEquals(encodedPassword, userCaptured.getPassword());
            assertEquals(user.getRoles().size(), userArgumentCaptor.getValue().getRoles().size());

            verify(passwordEncoder, times(1)).encode(any());
            verify(repository, times(1)).findByEmail(user.getEmail());
            verify(repository, times(1)).save(any());
        }

        @Test
        @DisplayName("Should throw exception when email is not unique")
        void shouldThrowsExceptionWhenEmailAlreadyExistsInOtherUser() {
            var user = mockUser();
            var encodedPassword = "encodedPassword123";
            var messageException = "Email already exists!";

            var otherUser = User.builder()
                    .id(UUID.randomUUID())
                    .name("other")
                    .email(user.getEmail())
                    .password("123456")
                    .roles(new HashSet<>())
                    .build();
            otherUser.addRole(new Role(1L, RoleName.BASIC, Set.of(otherUser)));

            doReturn(encodedPassword).when(passwordEncoder).encode(user.getPassword());
            doReturn(Optional.of(otherUser)).when(repository).findByEmail(emailArgumentCaptor.capture());

            var exception = assertThrows(IntegrityViolationException.class, () -> service.insert(user));
            assertEquals(messageException, exception.getMessage());

            verify(passwordEncoder, times(1)).encode(any());
            verify(repository, times(1)).findByEmail(any());
            verify(repository, times(0)).save(any());
        }
    }

    User mockUser() {
        var user = User.builder()
                .id(UUID.randomUUID())
                .name("test1")
                .email("test1@gmail.com")
                .password("123")
                .roles(new HashSet<>())
                .build();

        var role = new Role(1L, RoleName.BASIC, Set.of(user));
        user.addRole(role);
        return user;
    }

}