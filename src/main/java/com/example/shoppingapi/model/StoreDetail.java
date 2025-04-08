package com.example.shoppingapi.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "store_details")
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

    @ElementCollection(fetch = FetchType.EAGER)
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

    // Getter and Setter methods

    public Long getStoreDetailId() {
        return storeDetailId;
    }

    public void setStoreDetailId(Long storeDetailId) {
        this.storeDetailId = storeDetailId;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getReview() {
        return review;
    }

    public void setReview(BigDecimal review) {
        this.review = review;
    }

    public Integer getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Integer totalProducts) {
        this.totalProducts = totalProducts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAttachmentUrls() {
        return attachmentUrls;
    }

    public void setAttachmentUrls(List<String> attachmentUrls) {
        this.attachmentUrls = attachmentUrls;
    }

    public Integer getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
