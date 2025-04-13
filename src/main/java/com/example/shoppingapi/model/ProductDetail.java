package com.example.shoppingapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product_detail")
@Data
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long productDetailId;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne()
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "attachment_urls")
    private List<String> attachmentUrls;

    @Column(name = "total_sold")
    private Integer totalSold;

    @Column(name = "review_rating", precision = 3, scale = 2)
    private BigDecimal reviewRating;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMPTZ")
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

    @PreRemove
    public void preRemove() {
        this.deletedAt = LocalDateTime.now();
    }
}
