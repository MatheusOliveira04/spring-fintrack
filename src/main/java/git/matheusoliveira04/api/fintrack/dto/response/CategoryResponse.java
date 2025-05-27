package git.matheusoliveira04.api.fintrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse implements Serializable {
    private String id;
    private String description;
    private String type;
    private List<EntryResponse> entries;
    private String userId;
}
