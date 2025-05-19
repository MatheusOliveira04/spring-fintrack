package git.matheusoliveira04.api.fintrack.service;

import git.matheusoliveira04.api.fintrack.entity.User;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public interface UserService {

    User insert(@NotNull User user);

    User findById(@NotNull UUID id);

    Page<User> findAll(@PositiveOrZero int page, @Positive @Max(100) int size);

    User update(@NotNull User user);

    void delete(@NotNull UUID id);

    User findByEmail(@NotBlank String email);
}
