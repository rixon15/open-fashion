package com.openfashion.openfasion_marketplace.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Entity
@Table(name = "product_category")
@NoArgsConstructor
public class ProductCategory {

    @EmbeddedId
    private ProductCategoryKey id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    public ProductCategory(Product product, Category category) {
        this.product = product;
        this.category = category;
        this.id = new ProductCategoryKey(product.getId(), category.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategory that = (ProductCategory) o;
        return Objects.equals(product, that.product) &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, category);
    }
}
