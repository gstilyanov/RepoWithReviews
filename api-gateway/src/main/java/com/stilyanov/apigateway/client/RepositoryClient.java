package com.stilyanov.apigateway.client;

import com.stilyanov.apigateway.model.Repository;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("repository")
public interface RepositoryClient {

    @GetMapping("/cars")
    @CrossOrigin
    CollectionModel<Repository> readRepositories();

    
}
