package git.matheusoliveira04.api.fintrack.entity;

import git.matheusoliveira04.api.fintrack.entity.enums.CategoryName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryName type;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Entry> entries;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
