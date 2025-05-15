package git.matheusoliveira04.api.fintrack.service.impl;

import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.repository.CategoryRepository;
import git.matheusoliveira04.api.fintrack.service.CategoryService;
import git.matheusoliveira04.api.fintrack.service.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
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
}
