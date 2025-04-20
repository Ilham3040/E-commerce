package com.example.shoppingapi.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.type.YesNoConverter;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "store_categories")
@Data
@Builder(toBuilder = true)
@SoftDelete(columnName = "is_deleted", strategy = SoftDeleteType.DELETED, converter = YesNoConverter.class)
public class StoreCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName="id", nullable = false)
    private Store store;

    @Column(name = "category_name", length = 255, nullable = false)
    private String categoryName;

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
