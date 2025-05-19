package git.matheusoliveira04.api.fintrack.dto.request;

import git.matheusoliveira04.api.fintrack.entity.enums.CategoryName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @NotBlank
    private String description;
    @NotNull
    private CategoryName type;
}
