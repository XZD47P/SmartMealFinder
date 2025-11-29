package hu.project.smartmealfinderb.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userId", "recipeId"})
        }
)
public class FavouriteRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long recipeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public FavouriteRecipe(User user, Long recipeId) {
        this.recipeId = recipeId;
        this.user = user;
    }
}
