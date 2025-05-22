package git.matheusoliveira04.api.fintrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPageResponse implements Serializable {
    private List<CategoryResponse> categoryResponses;
    Long totalItems;
    Integer totalPages;
}
