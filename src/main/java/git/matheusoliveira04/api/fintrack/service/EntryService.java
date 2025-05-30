package git.matheusoliveira04.api.fintrack.service;

import git.matheusoliveira04.api.fintrack.entity.Entry;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
public interface EntryService {

    Entry insert(@NotNull @Valid Entry entry);

    List<Entry> findAllByUserId(@NotNull UUID userId);
}
