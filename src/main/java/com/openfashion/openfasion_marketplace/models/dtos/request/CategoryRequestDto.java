package com.openfashion.openfasion_marketplace.models.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {

    private String name;

    private String parent;

}
