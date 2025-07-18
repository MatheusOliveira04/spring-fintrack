package git.matheusoliveira04.api.fintrack.service.impl;

import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.repository.CategoryRepository;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    CategoryRepository repository;

    @InjectMocks
    CategoryServiceImpl service;

    @Captor
    ArgumentCaptor<Category> categoryCaptor;

    @Captor
    ArgumentCaptor<UUID> uuidCaptor;

    @Nested
    class insert {

        @Test
        @DisplayName("Should insert category with success")
        void shouldInsertCategoryWithSuccess() {
            var category = CategoryFactory.build();
            doReturn(category).when(repository).save(any());

            var output = service.insert(category);

            assertNotNull(output);
            assertEquals(category, output);
            assertEquals(category.getId(), output.getId());
            assertEquals(category.getType(), output.getType());
            assertEquals(category.getDescription(), output.getDescription());
            assertEquals(category.getUser(), output.getUser());
            assertEquals(category.getEntries().size(), output.getEntries().size());
        }

        @Test
        @DisplayName("Should pass correct parameters to save when inserting a new category with success")
        void shouldPassCorrectParametersWhenInsertingCategoryWithSuccess() {
            var category = CategoryFactory.build();
            doReturn(category).when(repository).save(categoryCaptor.capture());

            service.insert(category);

            var categoryCaptured = categoryCaptor.getValue();
            assertEquals(category.getId(), categoryCaptured.getId());
            assertEquals(category.getDescription(), categoryCaptured.getDescription());
            assertEquals(category.getType(), categoryCaptured.getType());
            assertEquals(category.getUser(), categoryCaptured.getUser());
            assertEquals(category.getEntries().size(), categoryCaptured.getEntries().size());

            verify(repository, times(1)).save(categoryCaptured);
        }

    }

    @Nested
    class findByIdAndUserId {

        @Test
        @DisplayName("Should find category by id and user id with success")
        void shouldFindCategoryWithSuccess() {
            var category = CategoryFactory.build();
            var categoryId = UUID.randomUUID();
            var userId = UUID.randomUUID();

            doReturn(Optional.of(category)).when(repository).findByIdAndUserId(any(), any());

            var categoryOutput = service.findByIdAndUserId(categoryId, userId);

            assertNotNull(categoryOutput);
            assertEquals(category, categoryOutput);
            assertEquals(category.getId(), categoryOutput.getId());
            assertEquals(category.getDescription(), categoryOutput.getDescription());
            assertEquals(category.getType(), categoryOutput.getType());
            assertEquals(category.getUser(), categoryOutput.getUser());
            assertEquals(category.getEntries().size(), categoryOutput.getEntries().size());

            verify(repository, times(1)).findByIdAndUserId(any(), any());
        }

        @Test
        @DisplayName("Should pass correct parameters when finding category by id and user id")
        void shouldPassCorrectParametersWhenFindingCategoryWithSuccess() {
            var category = CategoryFactory.build();
            var categoryId = UUID.randomUUID();
            var userId = UUID.randomUUID();

            doReturn(Optional.of(category)).when(repository).findByIdAndUserId(uuidCaptor.capture(), uuidCaptor.capture());

            service.findByIdAndUserId(categoryId, userId);

            var uuidCaptured = uuidCaptor.getAllValues();

            assertEquals(categoryId, uuidCaptured.getFirst());
            assertEquals(userId, uuidCaptured.get(1));

            verify(repository, times(1)).findByIdAndUserId(categoryId, userId);
        }

        @Test
        @DisplayName("Should throws ObjectNotFoundException when not found user")
        void shouldThrowsObjectNotFoundExceptionWhenNotFoundCategory() {
            var categoryId = UUID.randomUUID();
            var userId = UUID.randomUUID();
            var messageException = "Category not found with id: " + categoryId;

            doReturn(Optional.empty()).when(repository).findByIdAndUserId(any(), any());

            var exception = assertThrows(ObjectNotFoundException.class, () -> service.findByIdAndUserId(categoryId, userId));

            assertNotNull(exception);
            assertEquals(messageException, exception.getMessage());
            verify(repository, times(1)).findByIdAndUserId(any(), any());
        }
    }
}