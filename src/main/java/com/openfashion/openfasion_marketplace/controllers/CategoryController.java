package com.openfashion.openfasion_marketplace.controllers;

import com.openfashion.openfasion_marketplace.models.dtos.request.CategoryRequestDto;
import com.openfashion.openfasion_marketplace.models.dtos.request.DeleteCategoryRequestDto;
import com.openfashion.openfasion_marketplace.services.CategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public String addCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.addCategory(categoryRequestDto);
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestBody DeleteCategoryRequestDto deleteCategoryRequestDto) {
        return categoryService.deleteCategory(deleteCategoryRequestDto.getId());
    }

}
