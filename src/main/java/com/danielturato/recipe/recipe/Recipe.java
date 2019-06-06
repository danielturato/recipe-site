package com.danielturato.recipe.recipe;

import com.danielturato.recipe.category.Category;
import com.danielturato.recipe.ingrediant.Ingredient;
import com.danielturato.recipe.core.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Entity
public class Recipe extends BaseEntity {
    @Lob
    private Byte[] photo;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Category category;
    @NotNull
    private int prepTime;
    @NotNull
    private int cookTime;
    @ManyToMany
    private List<Ingredient> ingredients;
    @Lob
    private Map<Integer, String> steps;

    protected Recipe() {
        super();
        ingredients = new ArrayList<>();
        steps = new TreeMap<>();
    }

    public Recipe(String name, String description, Category category, int prepTime, int cookTime) {
        this();
        this.name = name;
        this.description = description;
        this.category = category;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void addStep(int step, String instruction) {
        steps.put(step, instruction);
    }

    public Byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(Byte[] photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Map<Integer, String> getSteps() {
        return steps;
    }

    public void setSteps(Map<Integer, String> steps) {
        this.steps = steps;
    }
}
