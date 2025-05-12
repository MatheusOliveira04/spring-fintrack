package git.matheusoliveira04.api.fintrack.service;

import git.matheusoliveira04.api.fintrack.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public interface UserService {

    User insert(@NotNull User user);

    User findById(@NotNull UUID id);

    Page<User> findAll(int page, int size);

    User update(@NotNull User user);

    void delete(@NotNull UUID id);
}
