package git.matheusoliveira04.api.fintrack.dto.response;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private String password;
    @Builder.Default
    private Set<String> roleNames = new HashSet<>();
}
