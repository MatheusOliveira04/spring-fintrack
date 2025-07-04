package git.matheusoliveira04.api.fintrack.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_user")
public class User implements Serializable {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Setter
    @Column(nullable = false)
    private String name;
    @Setter
    @Column(nullable = false)
    private String email;
    @Setter
    @Column(nullable = false)
    private String password;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Entry> entries = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Category> categories = new ArrayList<>();

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addAllRoles(Set<Role> roles) {
        roles.forEach(this::addRole);
    }

}
