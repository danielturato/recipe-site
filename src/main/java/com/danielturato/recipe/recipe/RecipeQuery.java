package com.danielturato.recipe.recipe;

import com.danielturato.recipe.category.Category;
import com.danielturato.recipe.ingredient.Ingredient;

public class RecipeQuery {

    private Category category;
    private Ingredient ingredient;
    private String name;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
