package com.danielturato.recipe.recipe;

import com.danielturato.recipe.exception.RecipeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository recipes;

    @Override
    public void save(Recipe recipe, byte[] photo) {
        recipe.setPhoto(photo);
        recipes.save(recipe);
    }

    @Override
    public void save(Recipe recipe, MultipartFile photo) {
        try {
            recipe.setPhoto(photo.getBytes());
            recipes.save(recipe);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Recipe findById(Long id) {
        Optional<Recipe> recipe = recipes.findById(id);
        if (recipe.isPresent()) {
            return recipe.get();
        } else {
            throw new RecipeNotFoundException("The recipe you're looking for cannot be found!");
        }
    }

    @Override
    public Recipe findByName(String name) {
        return null;
    }

    @Override
    public List<Recipe> findAll() {
        return (List<Recipe>) recipes.findAll();
    }

    @Override
    public void deleteById(Long id) {
        recipes.deleteById(id);
    }
}
