package git.matheusoliveira04.api.fintrack.mapper;

import git.matheusoliveira04.api.fintrack.dto.response.CategoryResponse;
import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CategoryMapper {

    @Mapping(target = "userId", ignore = true)
    CategoryResponse toCategoryResponseMapper(Category category);

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
