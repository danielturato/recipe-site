package com.danielturato.recipe.recipe;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {
    void save(Recipe recipe, byte[] photo);
    void save(Recipe recipe, MultipartFile photo);
    Recipe findById(Long id);
    Recipe findByName(String name);
    List<Recipe> findAll();
    void delete(Recipe recipe);
}
