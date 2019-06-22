package com.danielturato.recipe.recipe;

import com.danielturato.recipe.category.Category;
import com.danielturato.recipe.ingredient.Ingredient;
import com.danielturato.recipe.core.BaseEntity;
import com.danielturato.recipe.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Entity
public class Recipe extends BaseEntity {
    @Lob
    @JsonIgnore
    private byte[] photo;
    @NotNull
    @Size(min = 1, max = 24)
    private String name;
    @NotNull
    @Size(min = 1, max = 144)
    private String description;
    @NotNull
    private Category category;
    @NotNull
    @Min(0)
    private int prepTime;
    @NotNull
    @Min(0)
    private int cookTime;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;
    @ElementCollection
    private List<String> steps;
    @ManyToOne
    private User owner;

    public Recipe() {
        super();
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
    }

    public Recipe(String name, String description, Category category, int prepTime, int cookTime) {
        this();
        this.name = name;
        this.description = description;
        this.category = category;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void addStep(String instruction) {
        steps.add(instruction);
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
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

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }
}
