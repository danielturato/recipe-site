package com.danielturato.recipe.recipe;

import org.springframework.data.repository.CrudRepository;


public interface RecipeRepository extends CrudRepository<Recipe, Long> {

}
