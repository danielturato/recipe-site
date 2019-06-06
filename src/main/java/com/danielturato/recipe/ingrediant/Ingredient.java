package com.danielturato.recipe.ingrediant;

import com.danielturato.recipe.core.BaseEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Ingredient extends BaseEntity {
    @NotNull
    private String name;
    @NotNull
    private String condition;
    @NotNull
    private int quantity;

    protected Ingredient() {
        super();
    }

    public Ingredient(String name, String condition, int quantity) {
        this();
        this.name = name;
        this.condition = condition;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
