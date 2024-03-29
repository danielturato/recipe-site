package com.danielturato.recipe.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(exported = false)
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
