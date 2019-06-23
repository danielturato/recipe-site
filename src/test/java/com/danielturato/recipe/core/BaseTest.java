package com.danielturato.recipe.core;

import com.danielturato.recipe.category.Category;
import com.danielturato.recipe.ingredient.Ingredient;
import com.danielturato.recipe.recipe.Recipe;
import com.danielturato.recipe.user.User;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
public abstract class BaseTest  {

    protected User userBuilder() {
        User user = new User();
        user.setId(1L);
        user.setRoles(new String[]{"ROLE_USER", "ROLE_ADMIN"});
        user.setUsername("daniel");
        user.setPassword("password");

        return user;
    }

    protected List<Recipe> recipeListBuilder() {
        List<Recipe> recipes =  new ArrayList<>();
        recipes.add(recipeBuilder(1L));
        recipes.add(recipeBuilder(2L));

        return recipes;
    }

    protected List<Ingredient> ingredientsListBuilder() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredientBuilder());

        return ingredients;
    }

    protected Ingredient ingredientBuilder() {
        Ingredient ingredient = new Ingredient();
        ingredient.setCondition("good");
        ingredient.setName("test ing");
        ingredient.setQuantity(1);
        ingredient.setId(1L);

        return ingredient;
    }

    protected Recipe recipeBuilder(Long id)  {
        Recipe recipe = new Recipe();
        recipe.setName("Test recipe");
        recipe.setDescription("Test Description");
        recipe.setId(id);
        recipe.setCategory(Category.ALL_CATEGORIES);
        recipe.setCookTime(10);
        recipe.setPrepTime(10);
        recipe.addIngredient(ingredientBuilder());
        recipe.setOwner(userBuilder());
        try {
            recipe.setPhoto(getPhoto().getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return recipe;
    }

    protected MockMultipartFile getPhoto() {
        return new MockMultipartFile("image", "food.jpeg",
                "image/jpeg", "dummy content file".getBytes());
    }
}
