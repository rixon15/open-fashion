package com.openfashion.openfasion_marketplace.services;

import com.openfashion.openfasion_marketplace.models.dtos.request.CategoryRequestDto;
import com.openfashion.openfasion_marketplace.models.entities.Category;
import com.openfashion.openfasion_marketplace.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public String addCategory(CategoryRequestDto categoryRequestDto) {

        Category category = new Category();

        Optional<Category> existingCategory = categoryRepository.findByName(categoryRequestDto.getName());

        if (existingCategory.isPresent()) {
            return "Category with name " + categoryRequestDto.getName() + " already exists";
        }

        if(categoryRequestDto.getParent() == null) {

            category.setName(categoryRequestDto.getName());
            category.setParent(null);

            categoryRepository.save(category);
            return "Category added successfully";
        }

        Category parent = categoryRepository.findByName(categoryRequestDto.getParent())
                        .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(categoryRequestDto.getName());
        category.setParent(parent);
        categoryRepository.save(category);

        return "Category added successfully";
    }

    public String deleteCategory(Long id) {

        Optional<Category> categoryToDelete = categoryRepository.findById(id);

        if (categoryToDelete.isEmpty()) {
            return "Category with id " + id + " doest not exist";
        }

        categoryRepository.deleteById(id);

        return "Category deleted successfully";
    }
}
