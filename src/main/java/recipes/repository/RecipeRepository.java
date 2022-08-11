package recipes.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import recipes.entity.RecipeEntity;

import java.util.List;

public interface RecipeRepository extends JpaRepository<RecipeEntity,Long> { //CrudRepository

 @EntityGraph(attributePaths = {"user"})
   List<RecipeEntity> findByCategoryOrderByUpdateTimeDesc(String category);
   List<RecipeEntity> findByNameContainingIgnoreCaseOrderByUpdateTimeDesc(String name);

}
