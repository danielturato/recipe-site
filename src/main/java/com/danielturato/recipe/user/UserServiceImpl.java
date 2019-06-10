package com.danielturato.recipe.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository users;

    @Override
    public User findByUsername(String username) {
        return users.findByUsername(username);
    }
}
