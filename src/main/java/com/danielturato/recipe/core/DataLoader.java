package com.danielturato.recipe.core;

import com.danielturato.recipe.recipe.Recipe;
import com.danielturato.recipe.recipe.RecipeRepository;
import com.danielturato.recipe.user.User;
import com.danielturato.recipe.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    UserRepository users;

    @Autowired
    RecipeRepository recipes;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (users.findByUsername("admin") == null) {
            users.save(new User("admin", "password", new String[]{"ROLE_ADMIN", "ROLE_USER"}));
        }

    }
}
