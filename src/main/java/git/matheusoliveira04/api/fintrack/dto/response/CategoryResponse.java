package git.matheusoliveira04.api.fintrack.dto.response;

import git.matheusoliveira04.api.fintrack.entity.Entry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private String id;
    private String description;
    private String type;
    private List<Entry> entries;
    private String userId;
}
