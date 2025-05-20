package git.matheusoliveira04.api.fintrack.mapper;

import git.matheusoliveira04.api.fintrack.dto.response.CategoryPageResponse;
import git.matheusoliveira04.api.fintrack.dto.response.CategoryResponse;
import git.matheusoliveira04.api.fintrack.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryPageMapper {

    CategoryPageResponse toCategoryPageResponse(List<CategoryResponse> categoryResponses, Page<Category> categoryPage);
}
