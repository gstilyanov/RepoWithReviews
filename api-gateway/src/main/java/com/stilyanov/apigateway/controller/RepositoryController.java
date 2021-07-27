package com.stilyanov.apigateway.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.stilyanov.apigateway.client.RepositoryClient;
import com.stilyanov.apigateway.model.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class RepositoryController {

    private final RepositoryClient repositoryClient;

    public RepositoryController(RepositoryClient repositoryClient) {
        this.repositoryClient = repositoryClient;
    }

    private Collection<Repository> fallback() {
        return new ArrayList<>();
    }

    @GetMapping("/repositories")
    @CrossOrigin
    @HystrixCommand(fallbackMethod = "fallback")
    public Collection<Repository> repositories() {
        return new ArrayList<>(repositoryClient.readRepositories()
                .getContent());
    }
}
