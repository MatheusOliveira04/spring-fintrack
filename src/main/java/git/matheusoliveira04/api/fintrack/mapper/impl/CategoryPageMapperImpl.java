package git.matheusoliveira04.api.fintrack.mapper.impl;

import git.matheusoliveira04.api.fintrack.dto.response.CategoryPageResponse;
import git.matheusoliveira04.api.fintrack.dto.response.CategoryResponse;
import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.mapper.CategoryPageMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryPageMapperImpl implements CategoryPageMapper {

    @Override
    public CategoryPageResponse toCategoryPageResponse(List<CategoryResponse> categoryResponses, Page<Category> categoryPage) {
        return CategoryPageResponse
                .builder()
                .categoryResponses(categoryResponses)
                .totalItems(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .build();
    }
}
