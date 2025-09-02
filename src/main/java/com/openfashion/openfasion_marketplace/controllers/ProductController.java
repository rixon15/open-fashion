package com.openfashion.openfasion_marketplace.controllers;

import com.openfashion.openfasion_marketplace.models.dtos.request.ProductRequestDto;
import com.openfashion.openfasion_marketplace.models.dtos.response.ProductResponseDto;
import com.openfashion.openfasion_marketplace.services.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public String addProduct(@RequestBody ProductRequestDto productRequestDto){
     return productService.addProduct(productRequestDto);
    }

    @GetMapping("/{id}")
    public ProductResponseDto getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id, @RequestBody ProductRequestDto productRequestDto){
        return productService.updateProduct(id, productRequestDto);
    }

}
