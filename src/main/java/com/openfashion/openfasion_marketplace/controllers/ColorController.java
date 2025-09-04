package com.openfashion.openfasion_marketplace.controllers;

import com.openfashion.openfasion_marketplace.models.dtos.request.ColorRequestDto;
import com.openfashion.openfasion_marketplace.models.dtos.response.ColorResponseDto;
import com.openfashion.openfasion_marketplace.services.ColorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/color")
public class ColorController {

    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @PostMapping("/add")
    public String addColor(@RequestBody ColorRequestDto colorRequestDto) {
        return colorService.addColor(colorRequestDto);
    }

    @GetMapping("/{id}")
    public ColorResponseDto getColor(@PathVariable Long id) {
        return colorService.getColor(id);
    }

    @GetMapping("/all")
    public List<ColorResponseDto> getAllColors() {
        return colorService.getAllColors();
    }

    @DeleteMapping("/{id}")
    public String deleteColor(@PathVariable Long id) {
        return colorService.deleteColor(id);
    }

    @PutMapping("/{id}")
    public String updateColor(@PathVariable Long id, @RequestBody String hex_code) {
        return colorService.updateColor(id, hex_code);
    }

}
