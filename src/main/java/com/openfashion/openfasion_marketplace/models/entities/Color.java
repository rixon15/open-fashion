package com.openfashion.openfasion_marketplace.models.entities;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "color")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String hex_code;
}
