package com.danielturato.recipe.core;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    private final Long id;

    protected BaseEntity() {
        this.id = null;
    }

    public Long getId() {
        return id;
    }
}
