package com.danielturato.recipe.recipe;

import com.danielturato.recipe.category.Category;
import com.danielturato.recipe.ingredient.Ingredient;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {
    void save(Recipe recipe, byte[] photo);
    void save(Recipe recipe, MultipartFile photo);
    Recipe findById(Long id);
    Recipe findByName(String name);
    List<Recipe> findAll();
    void deleteById(Long id);
    List<Recipe> queryRecipes(RecipeQuery query);
}
