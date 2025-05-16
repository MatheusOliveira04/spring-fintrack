package git.matheusoliveira04.api.fintrack.service;

import git.matheusoliveira04.api.fintrack.entity.Category;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
public interface CategoryService {

    Category insert(@NotNull Category category);

    Category findById(@NotNull UUID id);

    List<Category> findAllByUserId(UUID id);
}
