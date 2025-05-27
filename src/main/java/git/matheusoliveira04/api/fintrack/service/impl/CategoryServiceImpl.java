package git.matheusoliveira04.api.fintrack.service.impl;

import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.repository.CategoryRepository;
import git.matheusoliveira04.api.fintrack.service.CategoryService;
import git.matheusoliveira04.api.fintrack.service.exception.ObjectNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @CacheEvict(value = "categories", allEntries = true)
    @Override
    public Category insert(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category findByIdAndUserId(UUID categoryId, UUID userId) {
        return categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found with id: " + categoryId));
    }

    @Cacheable(value = "categories", key = "#token + '-' + #page + '-' + #size")
    @Override
    public Page<Category> findAllByUserId(UUID userId, int page, int size) {
        Page<Category> allCategoriesByUserId = categoryRepository.findAllByUserId(userId, PageRequest.of(page, size));
        if (allCategoriesByUserId.isEmpty()) {
            throw new ObjectNotFoundException("No user category found!");
        }
        return allCategoriesByUserId;
    }

    @CacheEvict(value = "categories", allEntries = true)
    @Override
    public Category update(Category category) {
        findByIdAndUserId(category.getId(), category.getUser().getId());
        return categoryRepository.save(category);
    }

    @CacheEvict(value = "categories", allEntries = true)
    @Override
    public void delete(UUID categoryId, UUID userId) {
        categoryRepository.delete(findByIdAndUserId(categoryId, userId));
    }
}
