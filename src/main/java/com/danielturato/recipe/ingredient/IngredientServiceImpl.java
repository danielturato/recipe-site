package com.danielturato.recipe.ingredient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    IngredientRepository ingredients;

    @Override
    public List<Ingredient> findAll() {
        return (List<Ingredient>) ingredients.findAll();
    }
}
