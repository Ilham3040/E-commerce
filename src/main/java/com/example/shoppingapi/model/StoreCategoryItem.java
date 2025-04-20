package com.example.shoppingapi.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.type.YesNoConverter;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "store_categories_item")
@Data
@Builder(toBuilder = true)
@SoftDelete(columnName = "is_deleted", strategy = SoftDeleteType.DELETED, converter = YesNoConverter.class)
public class StoreCategoryItem {

    @EmbeddedId
    private StoreCategoryItemId id;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id",referencedColumnName="id", nullable = false)
    private StoreCategory storeCategory;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", referencedColumnName="id" ,nullable = false)
    private Product product;

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
