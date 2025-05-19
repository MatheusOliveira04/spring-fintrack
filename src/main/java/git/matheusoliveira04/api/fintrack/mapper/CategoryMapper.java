package git.matheusoliveira04.api.fintrack.mapper;

import git.matheusoliveira04.api.fintrack.dto.request.CategoryRequest;
import git.matheusoliveira04.api.fintrack.dto.response.CategoryResponse;
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
    Category toCategoryMapper(CategoryRequest categoryRequest);

    @Mapping(target = "userId", ignore = true)
    CategoryResponse toCategoryResponseMapper(Category category);

    default Category toCategory(CategoryRequest categoryRequest, @Context User user) {
        Category category = toCategoryMapper(categoryRequest);
        category.setUser(user);
        return category;
    }

    default CategoryResponse toCategoryResponse(Category category) {
        CategoryResponse categoryResponse = toCategoryResponseMapper(category);
        Optional.ofNullable(category.getUser())
                .map(User::getId)
                .map(Object::toString)
                .ifPresent(categoryResponse::setUserId);
        return categoryResponse;
    }

    default List<CategoryResponse> toCategoryResponse(List<Category> categories) {
        return categories
                .stream()
                .map(this::toCategoryResponse)
                .toList();
    }
}
