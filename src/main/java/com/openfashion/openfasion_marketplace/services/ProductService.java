package com.openfashion.openfasion_marketplace.services;

import com.openfashion.openfasion_marketplace.models.dtos.request.ProductRequestDto;
import com.openfashion.openfasion_marketplace.models.dtos.response.ProductResponseDto;
import com.openfashion.openfasion_marketplace.models.entities.*;
import com.openfashion.openfasion_marketplace.models.mappers.ProductMapper;
import com.openfashion.openfasion_marketplace.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ColorRepository colorRepository, SizeRepository sizeRepository, CategoryRepository categoryRepository, ProductCategoryRepository productCategoryRepository, ProductVariantRepository productVariantRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.categoryRepository = categoryRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productVariantRepository = productVariantRepository;
        this.productMapper = productMapper;
    }

    //Todo: Create stripe products as well after the payment integration (StripeService)

    @Transactional
    public String addProduct(ProductRequestDto productRequestDto){
        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());
        product.setBrand(productRequestDto.getBrand());

        productRepository.save(product);

        addProductVariant(productRequestDto, product);

        addCategory(productRequestDto, product);

        return "Product added successfully";

    }

    public ProductResponseDto getProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return productMapper.toProductResponseDto(product);

    }

    public String deleteProduct(Long id) {

        Optional<Product> existingProduct = productRepository.findById(id);

        if(existingProduct.isPresent()){
            productRepository.deleteById(id);

            return "Product deleted successfully";
        }

        return "Product with id: " + id + " not found";



    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(productRequestDto.getName());
        existingProduct.setDescription(productRequestDto.getDescription());
        existingProduct.setPrice(productRequestDto.getPrice());
        existingProduct.setBrand(productRequestDto.getBrand());
        productRepository.save(existingProduct);

        productVariantRepository.deleteAllByProductId(id);
        productVariantRepository.flush();

        addProductVariant(productRequestDto, existingProduct);

        productCategoryRepository.deleteAllByProductId(id);
        productCategoryRepository.flush();

        addCategory(productRequestDto, existingProduct);


        return productMapper.toProductResponseDto(existingProduct);
    }

    private void addCategory(ProductRequestDto productRequestDto, Product product) {
        productRequestDto.getCategoryNames().forEach(categoryName -> {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            ProductCategory newProductCategory = new ProductCategory();
            newProductCategory.setProduct(product);
            newProductCategory.setCategory(category);
            productCategoryRepository.save(newProductCategory);

        });
    }

    private void addProductVariant(ProductRequestDto productRequestDto, Product product) {
        productRequestDto.getVariants().forEach(variant -> {
            ProductVariant newProductVariant = new ProductVariant();

            Color color = colorRepository.findByName(variant.getColorName())
                    .orElseThrow(() -> new RuntimeException("Color not found"));

            Size size = sizeRepository.findByName(variant.getSizeName())
                    .orElseThrow(() -> new RuntimeException("Size not found"));

            newProductVariant.setProduct(product);
            newProductVariant.setSku(variant.getSku());
            newProductVariant.setStockQuantity(variant.getStockQuantity());
            newProductVariant.setColor(color);
            newProductVariant.setSize(size);

            productVariantRepository.save(newProductVariant);

        });
    }
}
