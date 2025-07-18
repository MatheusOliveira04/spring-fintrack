package git.matheusoliveira04.api.fintrack.repository;

import git.matheusoliveira04.api.fintrack.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Page<Category> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Category> findByIdAndUserId(UUID categoryId, UUID userId);
}
