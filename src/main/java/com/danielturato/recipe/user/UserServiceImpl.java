package com.danielturato.recipe.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository users;

    public UserServiceImpl(UserRepository users) {
        this.users = users;
    }

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
