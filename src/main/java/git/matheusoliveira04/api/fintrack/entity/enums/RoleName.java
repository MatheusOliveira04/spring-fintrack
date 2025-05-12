package git.matheusoliveira04.api.fintrack.entity.enums;

import lombok.Getter;

@Getter
public enum RoleName {
    ADMIN("Admin"),
    BASIC("Basic");

    private String description;

    RoleName(String description) {
        this.description = description;
    }
}
