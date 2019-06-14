package com.danielturato.recipe.user;

public interface UserService {
    User findByUsername(String username);
    void save(User user);
}
