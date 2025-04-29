package git.matheusoliveira04.api.fintrack.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String name;
    private String username;
    private String password;
    private Set<String> roleName;
}
