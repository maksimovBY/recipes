
package com.example.recipes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.recipes.dto.RecipeDto;
import com.example.recipes.exception.NotAllowedException;
import com.example.recipes.service.RecipeService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping("api/recipe/new")
    public Map<String, Long> saveRecipe(@Valid @RequestBody RecipeDto recipe) {
        Long nextId = recipeService.saveRecipe(recipe);
        return Map.of("id", nextId);
    }

    @GetMapping("api/recipe/{id}")
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable Long id) {
        RecipeDto recipe = recipeService.getRecipe(id);
        if (recipe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(recipe);
        }
    }


    @DeleteMapping("api/recipe/{id}")
    public ResponseEntity<Void> deleteRecipeById(@PathVariable Long id) {
        try {
            if (recipeService.deleteRecipe(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NotAllowedException notAllowedException) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<Void> updateRecipeById(@PathVariable Long id, @Valid @RequestBody RecipeDto recipe) {
        try {
            if (recipeService.updateRecipe(id, recipe)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NotAllowedException notAllowedException) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("api/recipe/search")
    public ResponseEntity<List<RecipeDto>> search(@RequestParam(required = false, name = "category") String
                                                          category,
                                                  @RequestParam(required = false, name = "name") String name) {

        if ((category == null && name == null) || (category != null && name != null)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (category != null) {
            return ResponseEntity.ok(recipeService.searchByCategory(category));
        } else {
            return ResponseEntity.ok(recipeService.searchByName(name));
        }
    }
}
