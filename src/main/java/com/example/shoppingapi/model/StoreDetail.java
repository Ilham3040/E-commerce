package com.example.shoppingapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "store_details")
@Getter
@Setter
public class StoreDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_detail_id")
    private Long storeDetailId;

    @OneToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "review", precision = 3, scale = 2)
    private BigDecimal review;

    @Column(name = "total_products")
    private Integer totalProducts;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "store_detail_attachment_urls", joinColumns = @JoinColumn(name = "store_detail_id"))
    @Column(name = "attachment_url", columnDefinition = "TEXT")
    private List<String> attachmentUrls;

    @Column(name = "follower_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer followerCount = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
