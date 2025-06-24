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
import static org.mockito.ArgumentMatchers.any;
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
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Captor
    private ArgumentCaptor<UUID> idCaptor;

    @Nested
    class insert {

        @Test
        @DisplayName("Should insert a user with success")
        void shouldInsertUserSuccess() {
            var user = mockUser();
            var encodedPassword = "encodedPassword123";

            doReturn(encodedPassword).when(passwordEncoder).encode(any());
            doReturn(Optional.empty()).when(repository).findByEmail(any());
            doReturn(user).when(repository).save(any());

            var userOutput = service.insert(user);

            assertNotNull(userOutput);
            assertEquals(user, userOutput);
            assertEquals(user.getId(), userOutput.getId());
            assertEquals(user.getName(), userOutput.getName());
            assertEquals(user.getEmail(), userOutput.getEmail());
            assertEquals(user.getPassword(), userOutput.getPassword());
            assertEquals(user.getRoles().size(), userOutput.getRoles().size());

            verify(passwordEncoder, times(1)).encode(any());
            verify(repository, times(1)).findByEmail(any());
            verify(repository, times(1)).save(any());
        }

        @Test
        @DisplayName("Should pass correct parameters to encoder, findByEmail and save")
        void shouldPassCorrectParametersWhenInsertingUserWithSuccess() {
            var user = mockUser();
            var originalUserPassword = user.getPassword();
            var encodedPassword = "encodedPassword123";

            doReturn(encodedPassword).when(passwordEncoder).encode(stringCaptor.capture());
            doReturn(Optional.empty()).when(repository).findByEmail(stringCaptor.capture());
            doReturn(user).when(repository).save(userCaptor.capture());

            service.insert(user);

            var valuesCaptured = stringCaptor.getAllValues();
            assertEquals(originalUserPassword, valuesCaptured.get(0));
            assertEquals(user.getEmail(), valuesCaptured.get(1));

            var userCaptured = userCaptor.getValue();
            assertEquals(user, userCaptured);
            assertEquals(user.getId(), userCaptured.getId());
            assertEquals(user.getName(), userCaptured.getName());
            assertEquals(user.getEmail(), userCaptured.getEmail());
            assertEquals(user.getPassword(), userCaptured.getPassword());
            assertEquals(user.getRoles().size(), userCaptured.getRoles().size());
        }

        @Test
        @DisplayName("should set password with password encoder before inserting user")
        void shouldSetPasswordWithEncoderBeforeInserting() {
            var user = mockUser();
            var originalUserPassword = user.getPassword();
            var encodedPassword = "encodedPassword123";

            doReturn(encodedPassword).when(passwordEncoder).encode(any());
            doReturn(Optional.empty()).when(repository).findByEmail(any());
            doReturn(user).when(repository).save(any());

            var userOutput = service.insert(user);

            assertEquals(encodedPassword, userOutput.getPassword());
            verify(passwordEncoder, times(1)).encode(originalUserPassword);
        }

        @Test
        @DisplayName("Should throw exception if email already exists")
        void shouldThrowExceptionWhenEmailExists() {
            var user = mockUser();
            var otherUser = User.builder()
                    .id(UUID.randomUUID())
                    .name("test1")
                    .email("test1@gmail.com")
                    .password("123")
                    .roles(new HashSet<>())
                    .build();

            doReturn(Optional.of(otherUser)).when(repository).findByEmail(any());

            var exception = assertThrows(IntegrityViolationException.class, () -> service.insert(user));
            assertNotNull(exception);
            assertEquals("Email already exists!", exception.getMessage());

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

            doReturn(Optional.of(user)).when(repository).findById(any());

            var userOutput = service.findById(user.getId());

            assertNotNull(userOutput);
            assertEquals(user, userOutput);
            assertEquals(user.getId(), userOutput.getId());
            assertEquals(user.getName(), userOutput.getName());
            assertEquals(user.getEmail(), userOutput.getEmail());
            assertEquals(user.getPassword(), userOutput.getPassword());
            assertEquals(user.getRoles().size(), userOutput.getRoles().size());

            verify(repository, times(1)).findById(any());
        }

        @Test
        @DisplayName("Should pass correct parameters to findById")
        void shouldPassCorrectParametersWhenFindingUserByIdWithSuccess() {
            var user = mockUser();

            doReturn(Optional.of(user)).when(repository).findById(idCaptor.capture());

            service.findById(user.getId());

            assertEquals(user.getId(), idCaptor.getValue());
        }

        @Test
        @DisplayName("should throws ObjectNotFoundException when not found user by id")
        void shouldThrowsObjectNotFoundExceptionWhenNotFoundUserById() {
            var user = mockUser();
            var messageException = "User not found with id: " + user.getId();

            doReturn(Optional.empty()).when(repository).findById(idCaptor.capture());

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