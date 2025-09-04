package com.openfashion.openfasion_marketplace.models.mappers;

import com.openfashion.openfasion_marketplace.models.dtos.response.ColorResponseDto;
import com.openfashion.openfasion_marketplace.models.entities.Color;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ColorMapper {

    @Mapping(target = "color", source = "color")
    ColorResponseDto toColorResponseDto(Color color);

    List<ColorResponseDto> toColorResponseDtoList(List<Color> colors);
}
