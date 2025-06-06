package git.matheusoliveira04.api.fintrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EntryRequest {
    @NotBlank
    private String description;
    @NotNull
    private BigDecimal value;
    @NotNull
    private LocalDate date;
    private Boolean paid;
    @NotBlank
    private String categoryId;
}
