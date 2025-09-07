package com.openfashion.openfasion_marketplace.models.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDetailsDto {

    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private String addressLine1;
    private String addressLine2 = null;

}
