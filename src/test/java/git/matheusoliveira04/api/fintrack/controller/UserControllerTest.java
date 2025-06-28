package git.matheusoliveira04.api.fintrack.controller;

import git.matheusoliveira04.api.fintrack.dto.response.UserResponse;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.factory.UserFactory;
import git.matheusoliveira04.api.fintrack.factory.UserPageResponseFactory;
import git.matheusoliveira04.api.fintrack.factory.UserResponseFactory;
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

import java.util.List;

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
    private ArgumentCaptor<List<User>> userListCaptor;

    @Captor
    private ArgumentCaptor<List<UserResponse>> userResponseListCaptor;

    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    @Captor
    private ArgumentCaptor<Page<User>> userPageCaptor;

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
}