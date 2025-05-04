package com.example.shoppingapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.type.YesNoConverter;

@Entity
@Table(name = "product_variants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@SoftDelete(columnName = "is_deleted", strategy = SoftDeleteType.DELETED, converter = YesNoConverter.class)
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long variantId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @Column(name = "variant_name", length = 255)
    private String variantName;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    @Builder.Default
    @Column(name = "stock_quantity", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer stockQuantity = 0;

    @Builder.Default
    @Column(name = "total_sold", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer totalSold = 0;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at",columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false,insertable = false, updatable = false)
    @Convert(converter = YesNoConverter.class)
    @Builder.Default
    private boolean isDeleted = false;

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
        deletedAt = LocalDateTime.now();
    }

}
