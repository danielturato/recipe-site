package com.danielturato.recipe.user;

import com.danielturato.recipe.core.BaseEntity;
import com.danielturato.recipe.recipe.Recipe;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends BaseEntity {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @NotNull
    private String username;
    @NotNull
    @JsonIgnore
    private String password;
    @JsonIgnore
    private List<String> roles;
    @ManyToMany
    private List<Recipe> favorites;

    protected User() {
        super();
        favorites = new ArrayList<>();
    }

    public User(@NotNull String username, @NotNull String password, List<String> roles) {
        this();
        this.username = username;
        setPassword(password);
        this.roles = roles;
    }

    public void addFavorite(Recipe recipe) {
        favorites.add(recipe);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Recipe> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Recipe> favorites) {
        this.favorites = favorites;
    }
}
