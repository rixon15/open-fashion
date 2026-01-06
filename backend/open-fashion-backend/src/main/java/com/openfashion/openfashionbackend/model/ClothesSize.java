package com.openfashion.openfashionbackend.model;

import com.openfashion.openfashionbackend.model.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class ClothesSize extends BaseEntity {

    @Column(nullable = false)
    private String name;

}
