package com.example.shoppingapi.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product_detail")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_detail_id")
    private Long productDetailId;

    @ManyToOne()
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "attachment_urls")
    private List<String> attachmentUrls;

    @Column(name = "total_sold")
    private Integer totalSold;

    @Column(name = "review_rating", precision = 3, scale = 2)
    private BigDecimal reviewRating;

    
    public Long getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(Long productDetailId) {
        this.productDetailId = productDetailId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<String> getAttachmentUrls() {
        return attachmentUrls;
    }

    public void setAttachmentUrls(List<String> attachmentUrls) {
        this.attachmentUrls = attachmentUrls;
    }

    public Integer getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(Integer totalSold) {
        this.totalSold = totalSold;
    }

    public BigDecimal getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(BigDecimal reviewRating) {
        this.reviewRating = reviewRating;
    }
}
