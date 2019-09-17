package com.danielturato.recipe.core;

import com.danielturato.recipe.user.User;
import com.danielturato.recipe.user.UserRepository;;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DataLoader implements ApplicationRunner {

    private final UserRepository users;

    public DataLoader(UserRepository users) {
        this.users = users;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (users.findByUsername("admin") == null) {
            users.save(new User("admin", "password", new String[]{"ROLE_ADMIN", "ROLE_USER"}));
        }

        if (users.findByUsername("daniel") == null) {
            users.save(new User("daniel", "password", new String[]{"ROLE_USER"}));
        }

    }
}
