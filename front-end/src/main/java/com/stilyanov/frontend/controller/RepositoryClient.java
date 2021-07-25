package com.stilyanov.frontend.controller;

import com.stilyanov.frontend.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Controller
public class RepositoryClient {

    @Value("${resourceserver.api.url}")
    private String resourceApiUrl;

    private final WebClient webClient;

    @Autowired
    public RepositoryClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/repositories")
    public String getRepositories(Model model) {
        List<Repository> repositories =
                webClient.get()
                .uri(resourceApiUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Repository>>() {
                })
                .block();
        model.addAttribute("repositories", repositories);
        return "repositories";
    }
}
