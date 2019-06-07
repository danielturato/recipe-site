package com.danielturato.recipe.recipe;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    @PreAuthorize("hasRole('ROLE_ADMIN') or #recipe?.owner.name == authentication.name")
    @Override
    <S extends Recipe> S save(@Param("recipe") S entity);

    @PreAuthorize("hasRole('ROLE_ADMIN') or @recipeRepository.findById(#id)?.owner.name == authentication.name")
    @Override
    void deleteById(@Param("id") Long aLong);

    @PreAuthorize("hasRole('ROLE_ADMIN') or #recipe?.owner.name == authentication.name")
    @Override
    void delete(@Param("recipe") Recipe entity);

    Recipe findByName(String name);

    @RestResource(exported = false)
    @Override
    Optional<Recipe> findById(Long aLong);
}
