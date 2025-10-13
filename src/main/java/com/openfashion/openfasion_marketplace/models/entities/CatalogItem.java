package com.openfashion.openfasion_marketplace.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "catalog_item")
public class CatalogItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Long stripeId;

    @OneToMany(mappedBy = "catalogItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants;


    @OneToMany(mappedBy = "catalogItem")
    @ToString.Exclude
    private Set<ProductCategory> productCategories = new HashSet<>();
}
