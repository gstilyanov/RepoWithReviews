package com.stilyanov.reviewservice.service;

import com.stilyanov.reviewservice.exception.InvalidOperationException;
import com.stilyanov.reviewservice.model.Dto.ReviewDto;
import com.stilyanov.reviewservice.model.Repository;
import com.stilyanov.reviewservice.model.Review;
import com.stilyanov.reviewservice.model.User;
import com.stilyanov.reviewservice.model.enums.State;
import com.stilyanov.reviewservice.repository.ReviewRepository;
import com.stilyanov.reviewservice.repository.UserRepository;
import com.stilyanov.reviewservice.service.contract.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public void softDelete(Long reviewId, User user) {
        Review review = reviewRepository.getById(reviewId);
        checkUserCanDeleteReview(review, userRepository.findByUsername(user.getUsername()));
        reviewRepository.deleteById(reviewId);
    }

    public void reviewRepository(ReviewDto reviewDto, User loggedUser, Repository repository) {
        List<Review> reviews = reviewRepository.getAllReviewsForRepository(repository.getId());
        Optional<Review> review = reviews.stream().filter(r -> r.getUser().getUsername()
                .equals(loggedUser.getUsername())).findFirst();
        if (review.isPresent()) {
            String reviewValue = reviewDto.getReview();
            review.get().setDescription(reviewValue);
            reviewRepository.save(review.get());
        } else {
            Review review1 = new Review();
            review1.setUser(loggedUser);
            review1.setRepository(repository);
            String reviewValue = reviewDto.getReview();
            review1.setDescription(reviewValue);
            review1.setState(State.ACTIVE);
            reviewRepository.saveAndFlush(review1);
        }
    }

    private void checkUserCanDeleteReview(Review review, User user) {
        if (review.getUser().getId() != user.getId()) {
            throw new InvalidOperationException(
                    String.format(
                            "User %s can't delete review - %s",
                            user.getUsername(),
                            review.getDescription()));
        }
    }
}
