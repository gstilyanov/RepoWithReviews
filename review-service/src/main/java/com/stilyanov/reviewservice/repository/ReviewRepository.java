package com.stilyanov.reviewservice.repository;

import com.stilyanov.reviewservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "select * from reviews r where r.repository_id = :repositoryId", nativeQuery = true)
    List<Review> getAllReviewsForRepository(long repositoryId);

}
