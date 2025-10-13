package com.openfashion.openfasion_marketplace.services;

import com.openfashion.openfasion_marketplace.models.dtos.request.CatalogItemRequestDto;
import com.openfashion.openfasion_marketplace.models.dtos.response.CatalogItemResponseDto;
import com.openfashion.openfasion_marketplace.models.entities.*;
import com.openfashion.openfasion_marketplace.models.mappers.CatalogItemMapper;
import com.openfashion.openfasion_marketplace.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CatalogItemService {

    private final CatalogItemRepository catalogItemRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CatalogItemMapper catalogItemMapper;

    public CatalogItemService(CatalogItemRepository catalogItemRepository, ColorRepository colorRepository, SizeRepository sizeRepository, CategoryRepository categoryRepository, ProductCategoryRepository productCategoryRepository, ProductVariantRepository productVariantRepository, CatalogItemMapper catalogItemMapper) {
        this.catalogItemRepository = catalogItemRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.categoryRepository = categoryRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productVariantRepository = productVariantRepository;
        this.catalogItemMapper = catalogItemMapper;
    }

    //Todo: Create stripe products as well after the payment integration (StripeService)

    @Transactional
    public String addProduct(CatalogItemRequestDto catalogItemRequestDto){
        CatalogItem catalogItem = new CatalogItem();
        catalogItem.setName(catalogItemRequestDto.getName());
        catalogItem.setDescription(catalogItemRequestDto.getDescription());
        catalogItem.setPrice(catalogItemRequestDto.getPrice());
        catalogItem.setBrand(catalogItemRequestDto.getBrand());

        catalogItemRepository.save(catalogItem);

        addProductVariant(catalogItemRequestDto, catalogItem);

        addCategory(catalogItemRequestDto, catalogItem);

        return "Product added successfully";

    }

    public CatalogItemResponseDto getProduct(Long id) {

        CatalogItem catalogItem = catalogItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return catalogItemMapper.toProductResponseDto(catalogItem);

    }

    public String deleteProduct(Long id) {

        Optional<CatalogItem> existingProduct = catalogItemRepository.findById(id);

        if(existingProduct.isPresent()){
            catalogItemRepository.deleteById(id);

            return "Product deleted successfully";
        }

        return "Product with id: " + id + " not found";



    }

    @Transactional
    public CatalogItemResponseDto updateProduct(Long id, CatalogItemRequestDto catalogItemRequestDto) {

        CatalogItem existingCatalogItem = catalogItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingCatalogItem.setName(catalogItemRequestDto.getName());
        existingCatalogItem.setDescription(catalogItemRequestDto.getDescription());
        existingCatalogItem.setPrice(catalogItemRequestDto.getPrice());
        existingCatalogItem.setBrand(catalogItemRequestDto.getBrand());
        catalogItemRepository.save(existingCatalogItem);

        productVariantRepository.deleteAllByCatalogItemId(id);
        productVariantRepository.flush();

        addProductVariant(catalogItemRequestDto, existingCatalogItem);

        productCategoryRepository.deleteAllByCatalogItemId(id);
        productCategoryRepository.flush();

        addCategory(catalogItemRequestDto, existingCatalogItem);


        return catalogItemMapper.toProductResponseDto(existingCatalogItem);
    }

    private void addCategory(CatalogItemRequestDto catalogItemRequestDto, CatalogItem catalogItem) {
        catalogItemRequestDto.getCategoryNames().forEach(categoryName -> {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            ProductCategory newProductCategory = new ProductCategory();
            newProductCategory.setCatalogItem(catalogItem);
            newProductCategory.setCategory(category);
            productCategoryRepository.save(newProductCategory);

        });
    }

    private void addProductVariant(CatalogItemRequestDto catalogItemRequestDto, CatalogItem catalogItem) {
        catalogItemRequestDto.getVariants().forEach(variant -> {
            ProductVariant newProductVariant = new ProductVariant();

            Color color = colorRepository.findByName(variant.getColorName())
                    .orElseThrow(() -> new RuntimeException("Color not found"));

            Size size = sizeRepository.findByName(variant.getSizeName())
                    .orElseThrow(() -> new RuntimeException("Size not found"));

            newProductVariant.setCatalogItem(catalogItem);
            newProductVariant.setSku(variant.getSku());
            newProductVariant.setStockQuantity(variant.getStockQuantity());
            newProductVariant.setColor(color);
            newProductVariant.setSize(size);

            productVariantRepository.save(newProductVariant);

        });
    }
}
