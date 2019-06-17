package com.danielturato.recipe.user;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    void save(User user);
    List<User> findAll();
}
