package com.danielturato.recipe.ingredient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredients;

    public IngredientServiceImpl(IngredientRepository ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public List<Ingredient> findAll() {
        return (List<Ingredient>) ingredients.findAll();
    }
}
