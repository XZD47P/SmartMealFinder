package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.FavouriteRecipe;
import hu.project.smartmealfinderb.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteRecipeRepository extends JpaRepository<FavouriteRecipe, Long> {
    FavouriteRecipe findByUserAndRecipeId(User user, Long id);

    List<FavouriteRecipe> findAllByUser(User user);

    boolean existsByUserAndRecipeId(User user, Long id);
}
