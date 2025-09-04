package com.openfashion.openfasion_marketplace.models.mappers;

import com.openfashion.openfasion_marketplace.models.dtos.response.SizeResponseDto;
import com.openfashion.openfasion_marketplace.models.entities.Size;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SizeMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "id", source = "id")
    SizeResponseDto toSizeResponseDto(Size size);

    List<SizeResponseDto> toSizeResponseDtoList(List<Size> size);

}
