package com.example.shoppingapi.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.type.YesNoConverter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


@Entity
@Table(name = "product_reviews")
@Data
@Builder(toBuilder = true)
@SoftDelete(columnName = "deleted_at", strategy = SoftDeleteType.DELETED, converter = YesNoConverter.class)
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long reviewId;

    @ManyToOne()
    @JoinColumn(name = "product_id" , referencedColumnName = "id",nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false)
    private User user;

    @Column(name = "star_rating")
    @Min(value = 1, message = "Star rating must be at least {value}")
    @Max(value = 5, message = "Star rating must be at most {value}")
    private Integer starRating;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "attacment_urls")
    private List<String> attachmentUrls;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at",insertable=false, updatable = false)
    private LocalDateTime deletedAt;

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

    
}
