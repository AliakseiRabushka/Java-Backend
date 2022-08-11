package recipes.service;

import recipes.dto.Recipe;
import java.util.List;

@SuppressWarnings("unused")

public interface RecipeService {
    long save(Recipe recipe);

    default Recipe get(Long id) {
        return null;
    }

    default boolean remove(Long id) {
        return false;
    }

    default boolean update(Long id, Recipe recipe) {
        return false;
    }
    default List<Recipe> searchByCategory(String category) {
        return List.of();
    }
    default List<Recipe> searchByName(String name) {
        return List.of();
    }
}
