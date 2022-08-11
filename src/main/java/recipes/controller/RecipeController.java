package recipes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.dto.Recipe;
import recipes.exception.NotAllowedException;
import recipes.service.RecipeServiceImp;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor

public class RecipeController {
    private final RecipeServiceImp service;

    @PostMapping("/api/recipe/new")
    public Map<String, Long> saveRecipe(@Valid @RequestBody Recipe recipe) {
        Long id = service.save(recipe);
        return Map.of("id", id);
    }

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        Recipe recipe = service.get(id);
        if (recipe == null) {
            return new ResponseEntity<>(NOT_FOUND);
        } else {
            return ResponseEntity.ok(recipe);
        }
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<Void> removeRecipe(@PathVariable Long id) { ///
        try {
        if (service.remove(id)) {
            return new ResponseEntity<>(NO_CONTENT);
        } else {
            return new ResponseEntity<>(NOT_FOUND);
        }
    } catch (NotAllowedException notAllowedException) {
            return new ResponseEntity<>(FORBIDDEN);
        }
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<Void> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {///
        try {
            if (service.remove(id)) {
                return new ResponseEntity<>(NO_CONTENT);
            } else {
                return new ResponseEntity<>(NOT_FOUND);
            }
        } catch (NotAllowedException notAllowedException) {
            return new ResponseEntity<>(FORBIDDEN);
        }
    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity<List<Recipe>> search(@RequestParam(required = false, name = "category") String category,
                                               @RequestParam(required = false, name = "name") String name) {
        if ((category == null && name == null) || (category != null && name != null)) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        if (category != null) {
            return ResponseEntity.ok(service.searchByCategory(category));

        } else {

        } return ResponseEntity.ok(service.searchByName(name));
    }
}
