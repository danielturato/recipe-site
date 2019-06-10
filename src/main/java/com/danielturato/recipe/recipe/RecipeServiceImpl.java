package com.danielturato.recipe.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipes;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipes) {
        this.recipes = recipes;
    }

    @Override
    @Transactional
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
        }

        // TODO:drt - Create new exception to handle this
        throw new RuntimeException();
    }

    @Override
    public Recipe findByName(String name) {
        return null;
    }

    @Override
    public List<Recipe> findAll() {
        return (List<Recipe>) recipes.findAll();
    }
}
