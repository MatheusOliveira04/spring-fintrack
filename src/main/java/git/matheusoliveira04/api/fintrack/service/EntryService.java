package git.matheusoliveira04.api.fintrack.service;

import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Validated
public interface EntryService {

    Entry insert(@NotNull @Valid Entry entry);

    Page<Entry> findAllByUserId(@NotNull UUID userId, @PositiveOrZero int page, @Positive @Max(100) int size);

    Entry findByIdAndUserId(@NotNull UUID entryId, @NotNull UUID userId);

    Entry update(@NotNull Entry entry);

    void delete(@NotNull UUID entryId, @NotNull UUID userId);

    Page<Entry> massInsertUsingImportFile(@NotNull MultipartFile file, @NotNull User user, @NotNull Pageable pageable);
}
