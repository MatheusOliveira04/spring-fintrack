package git.matheusoliveira04.api.fintrack.service;

import git.matheusoliveira04.api.fintrack.entity.Category;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public interface CategoryService {

    Category insert(@NotNull Category category);

    Category findByIdAndUserId(@NotNull UUID categoryId, @NotNull UUID userId);

    Page<Category> findAllByUserId(@NotNull UUID userId, @PositiveOrZero int page, @Positive @Max(100) int size);

    Category update(@NotNull Category category);

    void delete(@NotNull UUID categoryId, @NotNull UUID userId);
}
