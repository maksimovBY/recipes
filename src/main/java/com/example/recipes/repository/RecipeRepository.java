package com.example.recipes.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import com.example.recipes.entity.RecipeEntity;

import java.util.List;

public interface RecipeRepository extends CrudRepository<RecipeEntity, Long> {


    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"ingredients"
            }
    )
    List<RecipeEntity> findByCategoryIgnoreCaseOrderByDateDesc(String category);

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"ingredients"
            }
    )
    List<RecipeEntity> findByNameContainingIgnoreCaseOrderByDateDesc(String name);


}
