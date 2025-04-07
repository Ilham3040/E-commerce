package com.example.shoppingapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "product_reviews")
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne()
    @JoinColumn(name = "product_id" , referencedColumnName = "product_id",nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id",nullable = false)
    private User user;

    @Column(name = "star_rating")
    private Integer starRating;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "attacment_urls")
    private List<String> attachmentUrls;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column( name = "likes")
    private Integer likes = 0;


    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getStarRating() {
        return starRating;
    }

    public void setStarRating(Integer starRating) {
        this.starRating = starRating;
    }

    public List<String> getAttachmentUrls() {
        return attachmentUrls;
    }

    public void setAttachmentUrls(List<String> attachmentUrls) {
        this.attachmentUrls = attachmentUrls;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
    
}
