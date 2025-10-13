package com.openfashion.openfasion_marketplace.models.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryKey implements Serializable {

    private Long catalogItemId;
    private Long categoryId;

    // It's crucial to override equals() and hashCode() for composite keys!!!!!!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategoryKey that = (ProductCategoryKey) o;
        return Objects.equals(catalogItemId, that.catalogItemId) &&
                Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catalogItemId, categoryId);
    }
}
