package com.example.recipes.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.example.recipes.dto.RecipeDto;
import com.example.recipes.entity.RecipeEntity;
import com.example.recipes.entity.UserEntity;
import com.example.recipes.exception.NotAllowedException;
import com.example.recipes.repository.RecipeRepository;
import com.example.recipes.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Component
@Data
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeDto getRecipe(Long id) {
        Optional<RecipeEntity> recipeEntityByIdOptional = recipeRepository.findById(id);
        if (recipeEntityByIdOptional.isEmpty()) {
            return null;
        }
        RecipeEntity recipeEntityById = recipeEntityByIdOptional.get();

        RecipeDto recipeDtoById = new RecipeDto();
        recipeDtoById.setName(recipeEntityById.getName());
        recipeDtoById.setDescription(recipeEntityById.getDescription());
        recipeDtoById.setIngredients(recipeEntityById.getIngredients());
        recipeDtoById.setDirections(recipeEntityById.getDirections());
        recipeDtoById.setCategory(recipeEntityById.getCategory());
        recipeDtoById.setDate(recipeEntityById.getDate());
        return recipeDtoById;
    }

    public Long saveRecipe(RecipeDto recipe) {
        RecipeEntity recipeEntity = new RecipeEntity();
        setCommonFieldFromDtoToEntity(recipeEntity, recipe);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("user not found"));

        recipeEntity.setUser(user);

        return recipeRepository.save(recipeEntity).getId();
    }

    public boolean deleteRecipe(Long id) {
        if (recipeRepository.existsById(id)) {
            if (isRecipeOwnedByUser(id, SecurityContextHolder.getContext().getAuthentication())) {
                recipeRepository.deleteById(id);
                return true;
            } else {
                throw new NotAllowedException();
            }
        } else {
            return false;
        }
    }

    public boolean updateRecipe(Long id, RecipeDto newRecipe) {
        if (recipeRepository.existsById(id)) {
            if (isRecipeOwnedByUser(id, SecurityContextHolder.getContext().getAuthentication())) {
                RecipeEntity recipeEntity = new RecipeEntity();
                recipeEntity.setId(id);
                Optional<RecipeEntity> oldRecipe = recipeRepository.findById(id);
                oldRecipe.ifPresent(recipe -> recipeEntity.setUser(recipe.getUser()));
                setCommonFieldFromDtoToEntity(recipeEntity, newRecipe);
                recipeRepository.save(recipeEntity);
                return true;
            } else {
                throw new NotAllowedException();
            }
        }
        return false;
    }


    public List<RecipeDto> searchByCategory(String category) {
        return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category).stream()
                .map(r -> {
                    RecipeDto recipeDto = new RecipeDto();
                    setCommonFieldFromEntityToDto(r, recipeDto);
                    return recipeDto;
                })
                .collect(Collectors.toList());
    }

    public List<RecipeDto> searchByName(String name) {
        return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name).stream()
                .map(r -> {
                    RecipeDto recipeDto = new RecipeDto();
                    setCommonFieldFromEntityToDto(r, recipeDto);
                    return recipeDto;
                })
                .collect(Collectors.toList());
    }

    private void setCommonFieldFromDtoToEntity(RecipeEntity recipeEntity, RecipeDto newRecipe) {
        recipeEntity.setName(newRecipe.getName());
        recipeEntity.setDescription(newRecipe.getDescription());
        recipeEntity.setIngredients(newRecipe.getIngredients());
        recipeEntity.setDirections(newRecipe.getDirections());
        recipeEntity.setCategory(newRecipe.getCategory());
        recipeEntity.setDate(LocalDateTime.now());
    }

    private void setCommonFieldFromEntityToDto(RecipeEntity recipeEntity, RecipeDto recipe) {
        recipe.setName(recipeEntity.getName());
        recipe.setDescription(recipeEntity.getDescription());
        recipe.setIngredients(recipeEntity.getIngredients());
        recipe.setDirections(recipeEntity.getDirections());
        recipe.setCategory(recipeEntity.getCategory());
        recipe.setDate(recipeEntity.getDate());
    }

    private boolean isRecipeOwnedByUser(Long recipeId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return recipeRepository.findById(recipeId)
                .map(RecipeEntity -> RecipeEntity.getUser() != null &&
                        hasText(RecipeEntity.getUser().getEmail()) &&
                        RecipeEntity.getUser().getEmail().equalsIgnoreCase(userDetails.getUsername()))
                .orElse(false);
    }
}