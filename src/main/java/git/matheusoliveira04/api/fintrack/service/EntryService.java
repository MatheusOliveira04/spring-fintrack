package git.matheusoliveira04.api.fintrack.service;

import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Validated
public interface EntryService {

    Entry insert(@NotNull @Valid Entry entry);

    Page<Entry> findAllByUserId(@NotNull UUID userId, @PageableDefault(size = 10, page = 0) @NotNull Pageable pageable);

    Entry findByIdAndUserId(@NotNull UUID entryId, @NotNull UUID userId);

    Entry update(@NotNull Entry entry);

    void delete(@NotNull UUID entryId, @NotNull UUID userId);

    List<Entry> importFile(@NotNull MultipartFile file, @NotNull User user);

    Resource exportData(@NotNull UUID userId, @NotNull Pageable pageable, @NotBlank String acceptHeader);
}
