package git.matheusoliveira04.api.fintrack.factory.role;

import git.matheusoliveira04.api.fintrack.entity.Role;
import git.matheusoliveira04.api.fintrack.entity.enums.RoleName;

public class RoleFactory {

    public static Role build() {
        return Role.builder()
                .id(1L)
                .name(RoleName.BASIC)
                .build();
    }
}
