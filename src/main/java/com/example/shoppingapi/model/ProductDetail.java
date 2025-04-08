package com.example.shoppingapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product_detail")
@Data
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
}
