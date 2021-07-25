package com.stilyanov.frontend.model.dto;

import javax.validation.constraints.Size;

public class RepositoryDto {

    @Size(min = 2, max = 20, message = "Name should be between 2 & 20 symbols")
    String name;

    @Size(min = 2, max = 100, message = "Description should be between 2 & 100 symbols")
    String description;

    public RepositoryDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
