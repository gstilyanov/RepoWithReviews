package com.stilyanov.reviewservice.controller;

import com.stilyanov.reviewservice.exception.InvalidOperationException;
import com.stilyanov.reviewservice.model.Dto.ReviewDto;
import com.stilyanov.reviewservice.model.Repository;
import com.stilyanov.reviewservice.model.Review;

import com.stilyanov.reviewservice.model.User;
import com.stilyanov.reviewservice.repository.UserRepository;
import com.stilyanov.reviewservice.service.contract.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewsResource {

    private final WebClient webClient;
    private final ReviewService reviewService;
    private final UserRepository userRepository;

    @Autowired
    public ReviewsResource(WebClient webClient, ReviewService reviewService, UserRepository userRepository) {
        this.webClient = webClient;
        this.reviewService = reviewService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Review> getAll() {
        return reviewService.getAllReviews();
    }

    @PostMapping("/{id}")
    public Repository reviewRepository(@PathVariable long repositoryId, @Valid @RequestBody ReviewDto reviewDto,
                                       Principal principal) {

        Repository repository = getRepositoryById(repositoryId);

        User user = userRepository.findByUsername(principal.getName());
        reviewService.reviewRepository(reviewDto, user, repository);

        return repository;
    }

    @DeleteMapping("/{id}/delete")
    public void delete(@PathVariable long reviewId, Principal principal) {
        try {
            User user = userRepository.findByUsername(principal.getName());
            reviewService.softDelete(reviewId, user);
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    private Repository getRepositoryById(long repositoryId) {
        return webClient.get()
                .uri("http://repository/repositories/" + repositoryId)
                .retrieve()
                .bodyToMono(Repository.class)
                .block();
    }

}
