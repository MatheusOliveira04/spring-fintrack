package git.matheusoliveira04.api.fintrack.service.impl;

import git.matheusoliveira04.api.fintrack.entity.Role;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.entity.enums.RoleName;
import git.matheusoliveira04.api.fintrack.repository.UserRepository;
import git.matheusoliveira04.api.fintrack.service.exception.IntegrityViolationException;
import git.matheusoliveira04.api.fintrack.service.exception.ObjectNotFoundException;
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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

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
        @DisplayName("Should throws IntegrityViolationException when email is not unique")
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
            assertNotNull(exception);
            assertEquals(messageException, exception.getMessage());

            verify(passwordEncoder, times(1)).encode(any());
            verify(repository, times(1)).findByEmail(any());
            verify(repository, times(0)).save(any());
        }
    }

    @Nested
    class findById {

        @Test
        @DisplayName("Should find by id a user with success")
        void shouldFindByIdWithSuccess() {
            var user = mockUser();

            doReturn(Optional.of(user)).when(repository).findById(uuidArgumentCaptor.capture());

            var output = service.findById(user.getId());

            assertNotNull(output);
            assertEquals(user, output);
            assertEquals(user.getId(), uuidArgumentCaptor.getValue());
            verify(repository, times(1)).findById(any());
        }

        @Test
        @DisplayName("should throws ObjectNotFoundException when not found user by id")
        void shouldThrowsObjectNotFoundExceptionWhenNotFoundUserById() {
            var user = mockUser();
            var messageException = "User not found with id: " + user.getId();

            doReturn(Optional.empty()).when(repository).findById(uuidArgumentCaptor.capture());

            var exception = assertThrows(ObjectNotFoundException.class, () -> service.findById(user.getId()));
            assertNotNull(exception);
            assertEquals(messageException, exception.getMessage());
            verify(repository, times(1)).findById(any());
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