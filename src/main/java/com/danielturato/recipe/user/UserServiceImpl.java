package com.danielturato.recipe.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository users;

    @Override
    public User findByUsername(String username) {
        return users.findByUsername(username);
    }

    @Override
    public void save(User user) {
        users.save(user);
    }

    @Override
    public List<User> findAll() {
        return (List<User>) users.findAll();
    }
}
