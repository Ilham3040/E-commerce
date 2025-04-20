package com.example.shoppingapi.converter;

import jakarta.persistence.AttributeConverter;
import java.time.LocalDateTime;

public class DeletedAtConverter implements AttributeConverter<Boolean, LocalDateTime> {
    @Override
    public LocalDateTime convertToDatabaseColumn(Boolean attribute) {
        return Boolean.TRUE.equals(attribute) ? LocalDateTime.now() : null;
    }

    @Override
    public Boolean convertToEntityAttribute(LocalDateTime dbData) {
        return dbData != null;
    }
}
