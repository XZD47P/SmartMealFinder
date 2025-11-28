package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.LikedRecipe;
import hu.project.smartmealfinderb.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedRecipeRepository extends JpaRepository<LikedRecipe, Long> {
    boolean existsByUserAndRecipeId(User user, Long id);

    int countByRecipeId(Long recipeId);

    LikedRecipe getLikedRecipeByUserAndRecipeId(User user, Long id);
}
