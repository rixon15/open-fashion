package com.openfashion.openfasion_marketplace.models.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    private BigDecimal price;
    private String brand;
    private String name;
    private String description;

    private List<ProductVariantRequestDto> variants;

    private List<String> categoryNames;

}
/*
Example Json
{
        "price": 49.99,
        "brand": "Open Fashion",
        "name": "Stylish Shirt",
        "description": "A comfortable and stylish shirt.",
        "variants": [
            {
                "sku": "OF-SHIRT-M-BLUE",
                "stockQuantity": 50,
                "colorName": "Blue",
                "sizeName": "M"
            },
            {
                "sku": "OF-SHIRT-L-BLUE",
                "stockQuantity": 30,
                "colorName": "Blue",
                "sizeName": "L"
            }
        ],
        "categoryNames": [
            "Apparel",
            "Men's"
        ]
}
 */