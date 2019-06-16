package com.danielturato.recipe.category;

public enum Category {
    ALL_CATEGORIES("All Categories"),
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    DESSERT("Dessert");

    public final String label;

    private Category(String label) {
        this.label = label;
    }

}
