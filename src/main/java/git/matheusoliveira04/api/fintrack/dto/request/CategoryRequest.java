package git.matheusoliveira04.api.fintrack.dto.request;

import git.matheusoliveira04.api.fintrack.entity.enums.CategoryName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    private String description;
    private CategoryName type;
}
