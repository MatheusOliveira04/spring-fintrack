package git.matheusoliveira04.api.fintrack.service.impl;

import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.entity.enums.CategoryName;
import git.matheusoliveira04.api.fintrack.factory.user.UserFactory;

import java.util.List;
import java.util.UUID;

public class CategoryFactory {

    public static Category build() {
       return Category.builder()
               .id(UUID.randomUUID())
               .description("salary")
               .type(CategoryName.EXPENSIVE)
               .user(UserFactory.build())
               .entries(List.of())
               .build();
    }
}
