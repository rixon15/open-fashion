package com.openfashion.openfasion_marketplace.models.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "shipping_detail")
@EqualsAndHashCode(exclude = "order")
@ToString(exclude = "order")
public class ShippingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String addressLine1;

    @Column()
    private String addressLine2;

    @Column(nullable = false)
    private String city;

    @Column(name = "state_province")
    private String stateProvince;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String country;

}
