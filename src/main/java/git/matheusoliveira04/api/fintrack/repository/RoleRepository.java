package git.matheusoliveira04.api.fintrack.repository;

import git.matheusoliveira04.api.fintrack.entity.Role;
import git.matheusoliveira04.api.fintrack.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    List<Role> findByNameIn(List<RoleName> names);
}
