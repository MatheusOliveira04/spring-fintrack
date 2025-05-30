package git.matheusoliveira04.api.fintrack.repository;

import git.matheusoliveira04.api.fintrack.entity.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EntryRepository extends JpaRepository<Entry, UUID> {

    Page<Entry> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Entry> findByIdAndUserId(UUID entryId, UUID userId);
}
