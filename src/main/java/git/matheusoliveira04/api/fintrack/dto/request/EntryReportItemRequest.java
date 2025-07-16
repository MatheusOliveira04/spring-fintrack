package git.matheusoliveira04.api.fintrack.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EntryReportItemRequest {

    private String description;
    private Double value;
    private Boolean paid;
    private String type;
}
