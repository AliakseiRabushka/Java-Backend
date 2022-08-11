package recipes.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RecipeEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;

    @ElementCollection
    private List<String> ingredients;
    @ElementCollection
    private List<String> directions;

    private String category;
    private LocalDateTime updateTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

}
