package com.stilyanov.reviewservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stilyanov.reviewservice.model.enums.State;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@SQLDelete(sql = "UPDATE reviews SET state = 'DELETED' WHERE review_id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "state <> 'DELETED'")
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long review_id;

    private String description;

    @ManyToOne
    private Repository repository;

    @ManyToOne
    private User user;

    @JsonIgnore
    @Column
    @Enumerated(EnumType.STRING)
    private State state;

    public Review() {
    }

    public Review(long review_id, String description, Repository repository, User user) {
        this.review_id = review_id;
        this.description = description;
        this.repository = repository;
        this.user = user;
    }

    public long getReview_id() {
        return review_id;
    }

    public void setReview_id(long review_id) {
        this.review_id = review_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
