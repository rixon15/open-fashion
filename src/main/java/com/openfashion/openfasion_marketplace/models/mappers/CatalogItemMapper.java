package com.openfashion.openfasion_marketplace.models.mappers;

import com.openfashion.openfasion_marketplace.models.dtos.response.CatalogItemResponseDto;
import com.openfashion.openfasion_marketplace.models.dtos.response.ProductVariantResponseDto;
import com.openfashion.openfasion_marketplace.models.entities.CatalogItem;
import com.openfashion.openfasion_marketplace.models.entities.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CatalogItemMapper {

    // This method maps the entire Product object to the DTO.
    // The "variants" field is automatically mapped to the nested DTO list
    // by calling the second mapping method below.
    @Mapping(target = "variants", source = "variants")
    CatalogItemResponseDto toProductResponseDto(CatalogItem catalogItem);

    // This method maps a single ProductVariant entity to its DTO.
    // It's called for each item in the variants list.
    @Mapping(target = "color", source = "color.hex_code")
    @Mapping(target = "size", source = "size.name")
    ProductVariantResponseDto toProductVariantResponseDto(ProductVariant productVariant);

    // You can also add a list-to-list mapping for a complete solution.
    List<ProductVariantResponseDto> toProductVariantResponseDtoList(List<ProductVariant> productVariants);
}
