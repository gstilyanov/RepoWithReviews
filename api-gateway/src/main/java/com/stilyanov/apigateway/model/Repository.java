package com.stilyanov.apigateway.model;

import lombok.Data;

@Data
public class Repository {

    private Long id;
    private String name;
    private String description;
    private User owner;
}
