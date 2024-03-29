package com.danielturato.recipe.user;

import com.danielturato.recipe.core.BaseEntity;
import com.danielturato.recipe.recipe.Recipe;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
    private String[] roles;
    @ManyToMany
    private List<Recipe> favorites;

    public User() {
        super();
        favorites = new ArrayList<>();
    }

    public User(@NotNull String username, @NotNull String password, String[] roles) {
        this();
        this.username = username;
        setPassword(password);
        this.roles = roles;
    }

    public void addFavorite(Recipe recipe) {
        favorites.add(recipe);
    }

    public void removeFavorite(Recipe recipe) {
        favorites.remove(recipe);
    }

    public boolean isAFavorite(Recipe recipe) {
        return favorites.contains(recipe);
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

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public List<Recipe> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Recipe> favorites) {
        this.favorites = favorites;
    }
}
