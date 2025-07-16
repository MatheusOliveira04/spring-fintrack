package git.matheusoliveira04.api.fintrack.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EntryReportHeaderRequest {
    private String name;
    private String email;
}
