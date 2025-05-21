package git.matheusoliveira04.api.fintrack.service;

import git.matheusoliveira04.api.fintrack.entity.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public interface CategoryService {

    Category insert(@NotNull Category category);

    Category findById(@NotNull UUID id);

    Page<Category> findAllByUserId(@NotNull UUID userId, @PositiveOrZero int page, @Positive @Max(100) int size);

    Category update(@NotNull Category category);
}
