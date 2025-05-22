package git.matheusoliveira04.api.fintrack.service.impl;

import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.repository.CategoryRepository;
import git.matheusoliveira04.api.fintrack.service.CategoryService;
import git.matheusoliveira04.api.fintrack.service.exception.ObjectNotFoundException;
import git.matheusoliveira04.api.fintrack.util.TokenUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private TokenUtil tokenUtil;

    public CategoryServiceImpl(CategoryRepository categoryRepository, TokenUtil tokenUtil) {
        this.categoryRepository = categoryRepository;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public Category insert(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category findByIdAndUserId(UUID categoryId, UUID userId) {
        return categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found with id: " + categoryId));
    }

    @Override
    public Page<Category> findAllByUserId(UUID userId, int page, int size) {
        Page<Category> allCategoriesByUserId = categoryRepository.findAllByUserId(userId, PageRequest.of(page, size));
        if (allCategoriesByUserId.isEmpty()) {
            throw new ObjectNotFoundException("No user category found!");
        }
        return allCategoriesByUserId;
    }

    @Override
    public Category update(Category category) {
        findByIdAndUserId(category.getId(), category.getUser().getId());
        return categoryRepository.save(category);
    }

    @Override
    public void delete(UUID categoryId, UUID userId) {
        categoryRepository.delete(findByIdAndUserId(categoryId, userId));
    }
}
