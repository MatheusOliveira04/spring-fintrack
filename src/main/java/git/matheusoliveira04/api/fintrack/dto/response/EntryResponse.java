package git.matheusoliveira04.api.fintrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EntryResponse implements Serializable {
    private String id;
    private String description;
    private BigDecimal value;
    private LocalDate date;
    private Boolean paid;
    private CategoryResponse category;
    private String userId;
}
