package com.stilyanov.reviewservice.model.Dto;

import javax.validation.constraints.Size;

public class ReviewDto {

    @Size(min = 2, max = 100, message = "Review should be between 2 & 100 symbols")
    private String review;

    public ReviewDto() {
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
