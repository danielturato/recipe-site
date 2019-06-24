package com.danielturato.recipe.recipe;

import com.danielturato.recipe.category.Category;
import com.danielturato.recipe.ingredient.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    List<Recipe> findAllByCategory(Category category);

    List<Recipe> findAllByIngredients(Ingredient ingredient);

    @RestResource(path = "query-by")
    List<Recipe> findAllByCategoryAndIngredientsAndNameContaining(@Param("category")Category category,
                                                                    @Param("ingredient") Ingredient ingredient,
                                                                    @Param("name") String name);

}
