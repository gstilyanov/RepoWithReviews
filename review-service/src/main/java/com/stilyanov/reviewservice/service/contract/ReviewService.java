package com.stilyanov.reviewservice.service.contract;

import com.stilyanov.reviewservice.model.Dto.ReviewDto;
import com.stilyanov.reviewservice.model.Repository;
import com.stilyanov.reviewservice.model.Review;
import com.stilyanov.reviewservice.model.User;

import java.util.List;

public interface ReviewService {

    void softDelete(Long reviewId, User userDetails);

    void reviewRepository(ReviewDto reviewDto, User loggedUser, Repository repository);

    List<Review> getAllReviews();

}
