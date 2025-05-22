package git.matheusoliveira04.api.fintrack.service.impl;

import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.repository.CategoryRepository;
import git.matheusoliveira04.api.fintrack.service.CategoryService;
import git.matheusoliveira04.api.fintrack.service.exception.IntegrityViolationException;
import git.matheusoliveira04.api.fintrack.service.exception.ObjectNotFoundException;
import git.matheusoliveira04.api.fintrack.util.TokenUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
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
    public Category findById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found with id: " + id));
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
        findById(category.getId());
        return categoryRepository.save(category);
    }

    @Override
    public void delete(UUID categoryId, String token) {
        Category category = findById(categoryId);
        validateOwnership(category, getUserByToken(token));
        categoryRepository.delete(category);
    }

    private User getUserByToken(@NotBlank String token) {
        return tokenUtil.getUserByToken(token);
    }

    private void validateOwnership(@NotNull Category category, @NotNull User user) {
        UUID categoryId = validateAndGetCategoryUserId(category);
        UUID userId = validateAndGetUserId(user);
        if (!userId.equals(categoryId)) {
            throw new IntegrityViolationException("You are not authorized to access this category");
        }
    }

    private UUID validateAndGetUserId(User user) {
        return Optional.ofNullable(user)
                .map(User::getId)
                .orElseThrow(() -> new IntegrityViolationException("The user id not exists"));
    }

    private UUID validateAndGetCategoryUserId(Category category) {
        return Optional.ofNullable(category)
                .map(Category::getUser)
                .map(User::getId)
                .orElseThrow(() -> new IntegrityViolationException("The category user id not exists"));
    }

    @Override
    public Category findByIdAndValidateOwnership(UUID categoryId, UUID userId) {
        Category category = findById(categoryId);
        if (!userId.equals(category.getUser().getId())) {
            throw new IntegrityViolationException("You are not authorized to access this category");
        }

        return category;
    }
}
