package git.matheusoliveira04.api.fintrack.entity;

import git.matheusoliveira04.api.fintrack.entity.enums.CategoryName;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_category")
public class Category {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @Column(nullable = false)
    private String description;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryName type;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Entry> entries;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
