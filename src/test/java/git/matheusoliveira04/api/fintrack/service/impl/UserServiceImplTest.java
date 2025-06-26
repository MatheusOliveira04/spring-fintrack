package git.matheusoliveira04.api.fintrack.service.impl;

import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.factory.UserFactory;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    @Nested
    class insert {

        @Test
        @DisplayName("Should insert a user with success")
        void shouldInsertUserSuccess() {
            var user = UserFactory.build();
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
        @DisplayName("Should pass correct parameters to encoder, findByEmail and save when inserting a user")
        void shouldPassCorrectParametersWhenInsertingUserWithSuccess() {
            var user = UserFactory.build();
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
        @DisplayName("Should set password with password encoder before inserting user")
        void shouldSetPasswordWithEncoderBeforeInserting() {
            var user = UserFactory.build();
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
        @DisplayName("Should throw IntegrityViolationException if email already exists when inserting a user")
        void shouldThrowIntegrityViolationExceptionWhenEmailExists() {
            var user = UserFactory.build();
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
            var user = UserFactory.build();

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
            var user = UserFactory.build();

            doReturn(Optional.of(user)).when(repository).findById(idCaptor.capture());

            service.findById(user.getId());

            assertEquals(user.getId(), idCaptor.getValue());
        }

        @Test
        @DisplayName("Should throws ObjectNotFoundException when not found user by id")
        void shouldThrowsObjectNotFoundExceptionWhenNotFoundUserById() {
            var user = UserFactory.build();
            var messageException = "User not found with id: " + user.getId();

            doReturn(Optional.empty()).when(repository).findById(idCaptor.capture());

            var exception = assertThrows(ObjectNotFoundException.class, () -> service.findById(user.getId()));
            assertNotNull(exception);
            assertEquals(messageException, exception.getMessage());
            verify(repository, times(1)).findById(any());
        }
    }

    @Nested
    class findAll {

        @Test
        @DisplayName("should find all users with success")
        void shouldFindAllUsersWithSuccess() {
            var pageUser = new PageImpl<>(UserFactory.userListBuild());
            var page = PageRequest.of(0, 10);

            doReturn(pageUser).when(repository).findAll(any(Pageable.class));

            var listUser = service.findAll(page);

            assertEquals(pageUser.getSize(), listUser.getSize());
            assertEquals(pageUser.toList().get(0), listUser.toList().get(0));
            assertEquals(pageUser.toList().get(1), listUser.toList().get(1));
        }

        @Test
        @DisplayName("Should pass correct parameters to findAll method")
        void shouldPassCorrectParametersWhenFindingAllUsersWithSuccess() {
            var pageUser = new PageImpl<>(UserFactory.userListBuild());
            var page = PageRequest.of(0, 10);

            doReturn(pageUser).when(repository).findAll(pageableCaptor.capture());

            service.findAll(page);

            assertEquals(page, pageableCaptor.getValue());
            assertEquals(page.getPageSize(), pageableCaptor.getValue().getPageSize());
            assertEquals(page.getPageNumber(), pageableCaptor.getValue().getPageNumber());
        }

        @Test
        @DisplayName("Should throws ObjectNotFoundException when not found user")
        void shouldThrowsObjectNotFoundExceptionWhenNotFoundUser() {
            var page = PageRequest.of(0, 10);
            doReturn(new PageImpl<>(List.of())).when(repository).findAll(any(Pageable.class));

            var exception = assertThrows(ObjectNotFoundException.class, () -> service.findAll(page));
            assertNotNull(exception);
            assertEquals("No user found!", exception.getMessage());
        }
    }

    @Nested
    class update {

        @Test
        @DisplayName("Should update a user success when user exists")
        void shouldUpdateUserWithSuccess() {
            var user = UserFactory.build();
            var encodedPasswordUpdated = "encodedPasswordUpdated";

            var updatedUser = User.builder()
                    .id(user.getId())
                    .name("update")
                    .email("update@gmail.com")
                    .password("update")
                    .roles(new HashSet<>())
                    .build();

            doReturn(Optional.of(updatedUser)).when(repository).findById(any());
            doReturn(Optional.empty()).when(repository).findByEmail(any());
            doReturn(encodedPasswordUpdated).when(passwordEncoder).encode(any());
            doReturn(updatedUser).when(repository).save(any());

            var userOutput = service.update(user);

            assertNotNull(userOutput);
            assertEquals(updatedUser, userOutput);
            assertEquals(updatedUser.getId(), userOutput.getId());
            assertEquals(updatedUser.getName(), userOutput.getName());
            assertEquals(updatedUser.getEmail(), userOutput.getEmail());
            assertEquals(updatedUser.getPassword(), userOutput.getPassword());
            assertEquals(updatedUser.getRoles().size(), userOutput.getRoles().size());

            verify(repository, times(1)).findById(any());
            verify(repository, times(1)).findByEmail(any());
            verify(passwordEncoder, times(1)).encode(any());
            verify(repository, times(1)).save(any());
        }

        @Test
        @DisplayName("Should pass correct parameters to findById, findByEmail, encoder and save when updating a user")
        void shouldPassCorrectParametersWhenUpdatingUserWithSuccess() {
            var user = UserFactory.build();
            var originalUserPassword = user.getPassword();
            var encodedPasswordUpdated = "encodedPasswordUpdated";

            doReturn(Optional.of(user)).when(repository).findById(idCaptor.capture());
            doReturn(Optional.empty()).when(repository).findByEmail(stringCaptor.capture());
            doReturn(encodedPasswordUpdated).when(passwordEncoder).encode(stringCaptor.capture());
            doReturn(user).when(repository).save(userCaptor.capture());

            service.update(user);

            assertEquals(user.getId(), idCaptor.getValue());

            var stringValuesCaptured = stringCaptor.getAllValues();
            assertEquals(user.getEmail(), stringValuesCaptured.get(0));
            assertEquals(originalUserPassword, stringValuesCaptured.get(1));

            var userCaptured = userCaptor.getValue();
            assertEquals(user, userCaptured);
            assertEquals(user.getId(), userCaptured.getId());
            assertEquals(user.getName(), userCaptured.getName());
            assertEquals(user.getEmail(), userCaptured.getEmail());
            assertEquals(user.getPassword(), userCaptured.getPassword());
            assertEquals(user.getRoles().size(), userCaptured.getRoles().size());
        }

        @Test
        @DisplayName("Should throw ObjectNotFoundException when trying to update a user that does not exist")
        void shouldThrowObjectNotFoundExceptionWhenUpdatingNonExistentUser() {
            var user = UserFactory.build();
            var messageException = "User not found with id: " + user.getId();

            doReturn(Optional.empty()).when(repository).findById(any());

            var exception = assertThrows(ObjectNotFoundException.class, () -> service.update(user));
            assertNotNull(exception);
            assertEquals(messageException, exception.getMessage());
            verify(repository, times(1)).findById(any());
            verify(repository, times(0)).findByEmail(any());
            verify(passwordEncoder, times(0)).encode(any());
            verify(repository, times(0)).save(any());
        }

        @Test
        @DisplayName("Should throw IntegrityViolationException if email already exists when updating a user")
        void shouldThrowIntegrityViolationExceptionWhenEmailExistsWhenUpdatingUser() {
            var user = UserFactory.build();
            var updatedUser = User.builder()
                    .id(user.getId())
                    .name("update")
                    .email("update@gmail.com")
                    .password("update")
                    .roles(new HashSet<>())
                    .build();

            doReturn(Optional.of(user)).when(repository).findById(any());
            doReturn(Optional.of(updatedUser)).when(repository).findByEmail(any());

            var exception = assertThrows(IntegrityViolationException.class, () -> service.update(user));
            assertNotNull(exception);
            assertEquals("Email already exists!", exception.getMessage());

            verify(repository, times(1)).findById(any());
            verify(repository, times(1)).findByEmail(any());
            verify(passwordEncoder, times(0)).encode(any());
            verify(repository, times(0)).save(any());
        }

        @Test
        @DisplayName("Should set password with password encoder before updating user")
        void shouldSetPasswordWithEncoderBeforeUpdating() {
            var user = UserFactory.build();
            var originalUserPassword = user.getPassword();
            var encodedPassword = "encodedPassword123";

            doReturn(Optional.of(user)).when(repository).findById(any());
            doReturn(Optional.empty()).when(repository).findByEmail(any());
            doReturn(encodedPassword).when(passwordEncoder).encode(any());
            doReturn(user).when(repository).save(any());

            var userOutput = service.update(user);

            assertEquals(encodedPassword, userOutput.getPassword());
            verify(passwordEncoder, times(1)).encode(originalUserPassword);
        }
    }
}