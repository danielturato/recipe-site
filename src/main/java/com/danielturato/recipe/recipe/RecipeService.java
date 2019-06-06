package com.danielturato.recipe.recipe;

import java.util.List;

public interface RecipeService {
    void save(Recipe recipe);
    Recipe findById(Long id);
    Recipe findByName(String name);
    List<Recipe> findAll();
}
