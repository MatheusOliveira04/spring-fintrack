package git.matheusoliveira04.api.fintrack.dto.response;

import git.matheusoliveira04.api.fintrack.entity.User;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPageResponse {
    List<UserResponse> users;
    Long totalItems;
    Integer totalPages;
}
