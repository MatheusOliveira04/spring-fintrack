package git.matheusoliveira04.api.fintrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank @NotNull
    private String name;
    @NotBlank @NotNull
    private String username;
    @NotBlank @NotNull
    private String password;
    @NotNull @NotEmpty
    private Set<@NotBlank @NotNull String> roleName = new HashSet<>();
}
