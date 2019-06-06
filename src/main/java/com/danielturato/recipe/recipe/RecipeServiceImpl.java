package com.danielturato.recipe.recipe;

import com.danielturato.recipe.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipes;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipes) {
        this.recipes = recipes;
    }

    @Override
    public void save(Recipe recipe) {

    }

    @Override
    public Recipe findById(Long id) {
        return null;
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
