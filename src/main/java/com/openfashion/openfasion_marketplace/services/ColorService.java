package com.openfashion.openfasion_marketplace.services;

import com.openfashion.openfasion_marketplace.models.dtos.request.ColorRequestDto;
import com.openfashion.openfasion_marketplace.models.dtos.response.ColorResponseDto;
import com.openfashion.openfasion_marketplace.models.entities.Color;
import com.openfashion.openfasion_marketplace.models.mappers.ColorMapper;
import com.openfashion.openfasion_marketplace.repositories.ColorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColorService {

    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;

    public ColorService(ColorRepository colorRepository, ColorMapper colorMapper) {
        this.colorRepository = colorRepository;
        this.colorMapper = colorMapper;
    }

    public String addColor(ColorRequestDto colorRequestDto) {

        Optional<Color> existingColor = colorRepository.findByName(colorRequestDto.getName());

        if (existingColor.isPresent()) {
            return "Color already exists";
        }

        Color color = new Color();
        color.setName(colorRequestDto.getName());
        color.setHex_code(colorRequestDto.getHex_code());
        colorRepository.save(color);

        return "Color added successfully";

    }

    public ColorResponseDto getColorById(Long id) {

        Color existingColor = colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Color not found"));

        return colorMapper.toColorResponseDto(existingColor);

    }

    public List<ColorResponseDto> getAllColors() {

        List<Color> colors = colorRepository.findAll();

        return colorMapper.toColorResponseDtoList(colors);

    }

    public String deleteColor(Long id) {

        Optional<Color> existingColor = colorRepository.findById(id);

        if(existingColor.isEmpty()) {
            return "Color not found";
        }

        colorRepository.deleteById(id);

        return "Color deleted successfully";
    }

    public String updateColor(Long id, String hexCode) {

        Color existingColor = colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingColor.setHex_code(hexCode);
        colorRepository.save(existingColor);

        return "Color updated successfully";
    }

    public ColorResponseDto getColorByName(String value) {

        Color existingColor = colorRepository.findByName(value)
                .orElseThrow(() -> new RuntimeException("Color not found"));

        return colorMapper.toColorResponseDto(existingColor);

    }
}
