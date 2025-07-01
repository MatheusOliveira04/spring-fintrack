package git.matheusoliveira04.api.fintrack.controller;

import git.matheusoliveira04.api.fintrack.dto.request.UserRequest;
import git.matheusoliveira04.api.fintrack.dto.response.UserResponse;
import git.matheusoliveira04.api.fintrack.entity.Role;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.entity.enums.RoleName;
import git.matheusoliveira04.api.fintrack.factory.role.RoleFactory;
import git.matheusoliveira04.api.fintrack.factory.user.UserFactory;
import git.matheusoliveira04.api.fintrack.factory.user.UserPageResponseFactory;
import git.matheusoliveira04.api.fintrack.factory.user.UserRequestFactory;
import git.matheusoliveira04.api.fintrack.factory.user.UserResponseFactory;
import git.matheusoliveira04.api.fintrack.mapper.UserMapper;
import git.matheusoliveira04.api.fintrack.mapper.UserPageMapper;
import git.matheusoliveira04.api.fintrack.repository.RoleRepository;
import git.matheusoliveira04.api.fintrack.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    RoleRepository roleRepository;

    @Mock
    UserService service;

    @Mock
    UserMapper userMapper;

    @Mock
    UserPageMapper userPageMapper;

    @InjectMocks
    UserController controller;

    @Captor
    ArgumentCaptor<UUID> idCaptor;

    @Captor
    ArgumentCaptor<User> userCaptor;

    @Captor
    ArgumentCaptor<List<User>> userListCaptor;

    @Captor
    ArgumentCaptor<Page<User>> userPageCaptor;

    @Captor
    ArgumentCaptor<UserRequest> userRequestCaptor;

    @Captor
    ArgumentCaptor<List<UserResponse>> userResponseListCaptor;

    @Captor
    ArgumentCaptor<Pageable> pageableCaptor;

    @Captor
    ArgumentCaptor<List<RoleName>> roleNameListCaptor;

    @Captor
    ArgumentCaptor<Set<Role>> roleSetCaptor;

    @Nested
    class insert {

        @Test
        @DisplayName("Should return HTTP status CREATED and the correct UserResponse body")
        void shouldReturnHttpStatusCreated() {
            var setRole = List.of(RoleFactory.build());
            var user = UserFactory.build();
            var userResponse = UserResponseFactory.build();
            var uriBuilder = UriComponentsBuilder.fromPath("/api/v1/user");
            var userRequest = UserRequestFactory.build();

            doReturn(setRole).when(roleRepository).findByNameIn(any());
            doReturn(user).when(userMapper).toUser(any(), any());
            doReturn(user).when(service).insert(any());
            doReturn(userResponse).when(userMapper).toUserResponse(any());

            var response = controller.insert(userRequest, uriBuilder);

            assertNotNull(response);
            assertEquals(userResponse, response.getBody());
            assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());

            verify(roleRepository, times(1)).findByNameIn(any());
            verify(userMapper, times(1)).toUser(any(), any());
            verify(service, times(1)).insert(any());
            verify(userMapper, times(1)).toUserResponse(any());
        }

        @Test
        @DisplayName("Should pass correct parameters to dependency methods with success")
        void shouldPassCorrectParametersToDependencyMethodsWithSuccess() {
            var setRole = List.of(RoleFactory.build());
            var user = UserFactory.build();
            var userResponse = UserResponseFactory.build();
            var uriBuilder = UriComponentsBuilder.fromPath("/api/v1/user");
            var userRequest = UserRequestFactory.build();

            doReturn(setRole).when(roleRepository).findByNameIn(roleNameListCaptor.capture());
            doReturn(user).when(userMapper).toUser(userRequestCaptor.capture(), roleSetCaptor.capture());
            doReturn(user).when(service).insert(userCaptor.capture());
            doReturn(userResponse).when(userMapper).toUserResponse(userCaptor.capture());

            controller.insert(userRequest, uriBuilder);

            var roleNameListCaptured = roleNameListCaptor.getValue();
            assertEquals(1, roleNameListCaptured.size());
            assertEquals(RoleName.BASIC.getDescription(), roleNameListCaptured.getFirst().getDescription());

            assertEquals(userRequest, userRequestCaptor.getValue());
            assertEquals(setRole.size(), roleSetCaptor.getValue().size());
            assertEquals(setRole.getFirst(), roleSetCaptor.getValue().stream().toList().getFirst());

            assertEquals(user, userCaptor.getAllValues().get(0));
            assertEquals(user, userCaptor.getAllValues().get(1));

            verify(roleRepository, times(1)).findByNameIn(roleNameListCaptured);
            verify(userMapper, times(1)).toUser(any(), any());
            verify(service, times(1)).insert(any());
            verify(userMapper, times(1)).toUserResponse(any());
        }
    }

    @Nested
    class findById {

        @Test
        @DisplayName("Should return Http Status Ok and the correct UserResponse body")
        void shouldReturnHttpStatusOk() {
            var user = UserFactory.build();
            var userResponse = UserResponseFactory.build();

            doReturn(user).when(service).findById(any());
            doReturn(userResponse).when(userMapper).toUserResponse(any());

            var response = controller.findById(user.getId().toString());

            assertNotNull(response);
            assertNotNull(response.getBody());
            assertEquals(userResponse, response.getBody());
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

            verify(service, times(1)).findById(any());
            verify(userMapper, times(1)).toUserResponse(any());
        }

        @Test
        @DisplayName("Should pass correct parameters to dependency methods with success")
        void shouldPassCorrectParametersToDependencyMethodsWithSuccess() {
            var user = UserFactory.build();
            var userResponse = UserResponseFactory.build();

            doReturn(user).when(service).findById(idCaptor.capture());
            doReturn(userResponse).when(userMapper).toUserResponse(userCaptor.capture());

            controller.findById(user.getId().toString());

            assertEquals(user.getId(), idCaptor.getValue());

            assertEquals(user, userCaptor.getValue());
            assertEquals(user.getId(), userCaptor.getValue().getId());
            assertEquals(user.getName(), userCaptor.getValue().getName());
            assertEquals(user.getEmail(), userCaptor.getValue().getEmail());
            assertEquals(user.getPassword(), userCaptor.getValue().getPassword());

            verify(service, times(1)).findById(idCaptor.getValue());
            verify(userMapper, times(1)).toUserResponse(userCaptor.getValue());
        }

    }


    @Nested
    class findAll {

        @Test
        @DisplayName("Should return Http Status Ok")
        void shouldReturnHttpStatusOk() {
            var userPage = UserFactory.userPageBuild();
            var userResponseList = UserResponseFactory.userResponseListBuild();
            var userPageResponse = UserPageResponseFactory.build();

            doReturn(userPage).when(service).findAll(any());
            doReturn(userResponseList).when(userMapper).toUserResponses(any());
            doReturn(userPageResponse).when(userPageMapper).toUserPageResponse(any(), any());

            var response = controller.findAll(0, 10);

            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        }

        @Test
        @DisplayName("Should pass correct parameters to findAll service method")
        void shouldPassCorrectParametersToFindAllServiceMethod() {
            var page = 0;
            var size = 10;
            var userPage = UserFactory.userPageBuild();

            doReturn(userPage).when(service).findAll(pageableCaptor.capture());

            controller.findAll(page, size);

            var pageCaptured = pageableCaptor.getValue();
            assertEquals(PageRequest.of(page, size), pageCaptured);
            assertEquals(page, pageCaptured.getPageNumber());
            assertEquals(size, pageCaptured.getPageSize());
        }

        @Test
        @DisplayName("Should pass correct parameters to toUserResponses method when mapping users")
        void shouldPassCorrectParametersToToUserResponsesMapperMethod() {
            var page = 0;
            var size = 10;
            var userPage = UserFactory.userPageBuild();
            var userResponseList = UserResponseFactory.userResponseListBuild();

            doReturn(userPage).when(service).findAll(pageableCaptor.capture());
            doReturn(userResponseList).when(userMapper).toUserResponses(userListCaptor.capture());

            controller.findAll(page, size);

            assertEquals(userPage.getContent(), userListCaptor.getValue());
            assertEquals(userPage.toList().size(), userListCaptor.getValue().size());
        }

        @Test
        @DisplayName("Should pass correct parameters to toUserPageResponse method when mapping users")
        void shouldPassCorrectParametersToToUserPageResponseMapperMethod() {
            var page = 0;
            var size = 10;
            var userPage = UserFactory.userPageBuild();
            var userResponseList = UserResponseFactory.userResponseListBuild();
            var userPageResponse = UserPageResponseFactory.build();

            doReturn(userPage).when(service).findAll(pageableCaptor.capture());
            doReturn(userResponseList).when(userMapper).toUserResponses(userListCaptor.capture());
            doReturn(userPageResponse).when(userPageMapper).toUserPageResponse(userResponseListCaptor.capture(), userPageCaptor.capture());

            controller.findAll(page, size);

            assertEquals(userResponseList, userResponseListCaptor.getValue());
            assertEquals(userResponseList.size(), userResponseListCaptor.getValue().size());

            assertEquals(userPage, userPageCaptor.getValue());
            assertEquals(userPage.getTotalElements(), userPageCaptor.getValue().getTotalElements());
            assertEquals(userPage.getTotalPages(), userPageCaptor.getValue().getTotalPages());
        }

        @Test
        @DisplayName("Should call service.findAll, userMapper.toUserResponses and userPageMapper.toUserPageResponse")
        void shouldCallServiceAndMappersWhenFindingAllUsers() {
            var page = 0;
            var size = 10;
            var userPage = UserFactory.userPageBuild();
            var userResponseList = UserResponseFactory.userResponseListBuild();
            var userPageResponse = UserPageResponseFactory.build();

            doReturn(userPage).when(service).findAll(any());
            doReturn(userResponseList).when(userMapper).toUserResponses(any());
            doReturn(userPageResponse).when(userPageMapper).toUserPageResponse(any(), any());

            controller.findAll(page, size);

            verify(service, times(1)).findAll(any());
            verify(userMapper, times(1)).toUserResponses(any());
            verify(userPageMapper, times(1)).toUserPageResponse(any(), any());
        }

        @Test
        @DisplayName("Should return Response Body correctly")
        void shouldReturnResponseBodyCorrectly() {
            var page = 0;
            var size = 10;
            var userPage = UserFactory.userPageBuild();
            var userResponseList = UserResponseFactory.userResponseListBuild();
            var userPageResponse = UserPageResponseFactory.build();

            doReturn(userPage).when(service).findAll(pageableCaptor.capture());
            doReturn(userResponseList).when(userMapper).toUserResponses(userListCaptor.capture());
            doReturn(userPageResponse).when(userPageMapper).toUserPageResponse(userResponseListCaptor.capture(), userPageCaptor.capture());

            var response = controller.findAll(page, size);

            assertNotNull(response);
            assertNotNull(response.getBody());
            assertNotNull(response.getBody().getUsers());
            assertNotNull(response.getBody().getTotalItems());
            assertNotNull(response.getBody().getTotalPages());

            assertEquals(userPageResponse.getUsers().size(), response.getBody().getUsers().size());
            assertEquals(userPageResponse.getTotalItems(), response.getBody().getTotalItems());
            assertEquals(userPageResponse.getTotalPages(), response.getBody().getTotalPages());
        }
    }

    @Nested
    class update {

        @Test
        @DisplayName("Should return HTTP status OK and the correct UserResponse body")
        void shouldReturnHttpStatusOK() {
            var setRole = List.of(RoleFactory.build());
            var user = UserFactory.build();
            var userResponse = UserResponseFactory.build();

            var userRequest = UserRequestFactory.mockUpdated();

            doReturn(setRole).when(roleRepository).findByNameIn(any());
            doReturn(user).when(userMapper).toUser(any(), any());
            doReturn(user).when(service).update(any());
            doReturn(userResponse).when(userMapper).toUserResponse(any());

            var response = controller.update(user.getId().toString(), userRequest);

            assertNotNull(response);
            assertEquals(userResponse, response.getBody());
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

            verify(roleRepository, times(1)).findByNameIn(any());
            verify(userMapper, times(1)).toUser(any(), any());
            verify(service, times(1)).update(any());
            verify(userMapper, times(1)).toUserResponse(any());
        }

        @Test
        @DisplayName("Should pass correct parameters to dependency methods with success")
        void shouldPassCorrectParametersToDependencyMethodsWithSuccess() {
            var setRole = List.of(RoleFactory.build());
            var user = UserFactory.build();
            var userResponse = UserResponseFactory.build();

            var userRequest = UserRequestFactory.mockUpdated();

            doReturn(setRole).when(roleRepository).findByNameIn(roleNameListCaptor.capture());
            doReturn(user).when(userMapper).toUser(userRequestCaptor.capture(), roleSetCaptor.capture());
            doReturn(user).when(service).update(userCaptor.capture());
            doReturn(userResponse).when(userMapper).toUserResponse(userCaptor.capture());

            var response = controller.update(user.getId().toString(), userRequest);

            verify(roleRepository, times(1)).findByNameIn(roleNameListCaptor.getValue());
            verify(userMapper, times(1)).toUser(userRequestCaptor.getValue(), roleSetCaptor.getValue());
            verify(service, times(1)).update(userCaptor.getAllValues().getFirst());
            verify(userMapper, times(1)).toUserResponse(userCaptor.getAllValues().getLast());
        }
    }

    @Nested
    class delete {

        @Test
        @DisplayName("Should return Http Status no content")
        void shouldReturnHttpStatusNoContent() {
            var id = UUID.randomUUID();

            doNothing().when(service).delete(any());

            var response = controller.delete(id.toString());

            assertNotNull(response);
            assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());

            verify(service, times(1)).delete(id);
        }

        @Test
        @DisplayName("Should pass correct parameters to dependency methods with success")
        void shouldPassCorrectParametersToDependencyMethodsWithSuccess() {
            var id = UUID.randomUUID();

            doNothing().when(service).delete(idCaptor.capture());

            var response = controller.delete(id.toString());

            assertEquals(id, idCaptor.getValue());
            verify(service, times(1)).delete(eq(id));
        }
    }
}