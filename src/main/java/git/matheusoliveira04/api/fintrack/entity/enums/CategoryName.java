package git.matheusoliveira04.api.fintrack.entity.enums;

import lombok.Getter;

@Getter
public enum CategoryName {
    REVENUE("Revenue"),
    EXPENSIVE("Expensive");

    private String description;

    CategoryName(String description) {
        this.description = description;
    }
}
