package com.openfashion.openfasion_marketplace.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Float price;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "product")
    private Set<ProductCategory> productCategories = new HashSet<>();
}
