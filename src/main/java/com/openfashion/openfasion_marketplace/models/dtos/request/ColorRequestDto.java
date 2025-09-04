package com.openfashion.openfasion_marketplace.models.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorRequestDto {

    private String name;
    private String hex_code;

}
