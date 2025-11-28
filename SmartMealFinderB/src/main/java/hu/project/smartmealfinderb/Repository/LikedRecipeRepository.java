package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.LikedRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedRecipeRepository extends JpaRepository<LikedRecipe, Long> {
}
