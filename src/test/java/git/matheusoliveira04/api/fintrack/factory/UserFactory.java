package git.matheusoliveira04.api.fintrack.factory;

import git.matheusoliveira04.api.fintrack.entity.Role;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.entity.enums.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class UserFactory {

    public static User build() {
        var user = User.builder()
                .id(UUID.randomUUID())
                .name("test1")
                .email("test1@gmail.com")
                .password("123")
                .roles(new HashSet<>())
                .build();

        var role = new Role(1L, RoleName.BASIC, Set.of(user));
        user.addRole(role);
        return user;
    }

    public static Page<User> userPageBuild() {
        var user = User.builder()
                .id(UUID.randomUUID())
                .name("test2")
                .email("test2@gmail.com")
                .password("123")
                .roles(new HashSet<>())
                .build();
        return new PageImpl<>(List.of(build(), user));
    }
}
