package git.matheusoliveira04.api.fintrack.service;

import git.matheusoliveira04.api.fintrack.entity.Entry;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public interface EntryService {

    Entry insert(@NotNull @Valid Entry entry);

    Page<Entry> findAllByUserId(@NotNull UUID userId, @PositiveOrZero int page, @Positive @Max(100) int size);

    Entry findByIdAndUserId(@NotNull UUID entryId, @NotNull UUID userId);

    Entry update(@NotNull Entry entry);
}
