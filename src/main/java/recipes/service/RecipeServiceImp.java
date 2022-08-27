package recipes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import recipes.dto.Recipe;
import recipes.entity.RecipeEntity;
import recipes.entity.UserEntity;
import recipes.exception.NotAllowedException;
import recipes.repository.RecipeRepository;
import recipes.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Component

public class RecipeServiceImp implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
@Override
   public long save(Recipe recipe){
       RecipeEntity recipeEntity = RecipeEntity.builder()
               .name(recipe.getName())
               .description(recipe.getDescription())
               .ingredients(recipe.getIngredients())
               .directions(recipe.getDirections())
               .updateTime(recipe.getUpdateTime())
               .category(recipe.getCategory())
               .build();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("user not found"));

    recipeEntity.setUser(user);

    RecipeEntity savedRecipeEntity = recipeRepository.save(recipeEntity);
       return savedRecipeEntity.getId();
   }
    @Override
    public Recipe get(Long id){
       Optional<RecipeEntity> recipeById = recipeRepository.findById(id);
       if(recipeById.isEmpty()){
           return null;
       }
       RecipeEntity recipeEntity = recipeById.get();
        Recipe recipe = Recipe.builder()
                .name(recipeEntity.getName())
                .description(recipeEntity.getDescription())
                .ingredients(recipeEntity.getIngredients())
                .directions(recipeEntity.getDirections())
                .build();
       return recipe;
    }
    @Override
    public boolean remove(Long id) {
        if (recipeRepository.existsById(id)) {
            if (isRecipeOwnerByUser(id, SecurityContextHolder.getContext().getAuthentication())) {
                recipeRepository.deleteById(id);
                return true;
            } else {
                throw new NotAllowedException();
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean update(Long id, Recipe recipe) {
        if(recipeRepository.existsById(id)) {
            if (isRecipeOwnerByUser(id, SecurityContextHolder.getContext().getAuthentication())) {
                RecipeEntity recipeEntity = new RecipeEntity();
                recipeEntity.setId(id);
                setCommonFieldFromDtoToEntity(recipe, recipeEntity); // To
                recipeRepository.save(recipeEntity);
                return true;
            } else {
                throw new NotAllowedException();
            }
        }
        return false;
    }
    private RecipeEntity setCommonFieldFromDtoToEntity(Recipe recipe, RecipeEntity recipeEntity){
        return RecipeEntity.builder()
                .name(recipe.getName())
                .description(recipe.getDescription())
                .ingredients(recipe.getIngredients())
                .directions(recipe.getDirections())
                .build();
    }
    private Recipe setCommonFieldFromEntityToDto(RecipeEntity recipeEntity){
        return Recipe.builder()
                .name(recipeEntity.getName())
                .description(recipeEntity.getDescription())
                .ingredients(recipeEntity.getIngredients())
                .directions(recipeEntity.getDirections())
                .build();
    }
    @Override
    public List<Recipe> searchByCategory(String category) {
        return recipeRepository.findByCategoryOrderByUpdateTimeDesc(category).stream()
                .map(this::setCommonFieldFromEntityToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<Recipe> searchByName(String name) {
        return recipeRepository.findByNameContainingIgnoreCaseOrderByUpdateTimeDesc(name).stream()
                .map(this::setCommonFieldFromEntityToDto)
                .collect(Collectors.toList());
    }
    private boolean isRecipeOwnerByUser(Long recipeId, Authentication authentication){
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return recipeRepository.findById(recipeId)
            .map(recipeEntity -> recipeEntity.getUser() != null &&
                    hasText(recipeEntity.getUser().getEmail()) &&
                    recipeEntity.getUser().getEmail().equalsIgnoreCase(userDetails.getUsername()))
            .orElse(false);
    }
}