package git.matheusoliveira04.api.fintrack.mapper;

import git.matheusoliveira04.api.fintrack.dto.request.CategoryRequest;
import git.matheusoliveira04.api.fintrack.dto.response.CategoryResponse;
import git.matheusoliveira04.api.fintrack.dto.response.EntryResponse;
import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.entity.User;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "entries", ignore = true)
    Category toCategory(CategoryRequest categoryRequest, @Context User user);

    @Mapping(target = "userId", ignore = true)
    CategoryResponse toCategoryResponse(Category category);

    List<CategoryResponse> toCategoryResponse(List<Category> categories);

    @AfterMapping
    default void mapToCategory(
            @MappingTarget Category category,
            CategoryRequest categoryRequest,
            @Context User user) {
        category.setUser(user);
    }

    @AfterMapping
    default void mapToCategoryResponse(@MappingTarget CategoryResponse categoryResponse, Category category) {
        Optional.ofNullable(category.getUser())
                .map(User::getId)
                .map(Object::toString)
                .ifPresent(categoryResponse::setUserId);
    }

}
