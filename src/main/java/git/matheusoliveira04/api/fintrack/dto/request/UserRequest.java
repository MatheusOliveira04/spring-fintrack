package git.matheusoliveira04.api.fintrack.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank @NotNull
    private String name;
    @NotBlank @NotNull @Email
    private String email;
    @NotBlank @NotNull
    private String password;
    @NotNull @NotEmpty
    private Set<@NotBlank @NotNull String> roleNames = new HashSet<>();
}
