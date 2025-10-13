package com.openfashion.openfasion_marketplace.controllers;

import com.openfashion.openfasion_marketplace.models.dtos.request.CatalogItemRequestDto;
import com.openfashion.openfasion_marketplace.models.dtos.response.CatalogItemResponseDto;
import com.openfashion.openfasion_marketplace.services.CatalogItemService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
public class CatalogItemController {

    private final CatalogItemService catalogItemService;

    public CatalogItemController(CatalogItemService catalogItemService) {
        this.catalogItemService = catalogItemService;
    }

    @PostMapping("/add")
    public String addProduct(@RequestBody CatalogItemRequestDto catalogItemRequestDto){
     return catalogItemService.addProduct(catalogItemRequestDto);
    }

    @GetMapping("/{id}")
    public CatalogItemResponseDto getProduct(@PathVariable Long id) {
        return catalogItemService.getProduct(id);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        return catalogItemService.deleteProduct(id);
    }

    @PutMapping("/{id}")
    public CatalogItemResponseDto updateProduct(@PathVariable Long id, @RequestBody CatalogItemRequestDto catalogItemRequestDto){
        return catalogItemService.updateProduct(id, catalogItemRequestDto);
    }

}
