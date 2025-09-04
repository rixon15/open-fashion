package com.openfashion.openfasion_marketplace.controllers;

import com.openfashion.openfasion_marketplace.models.dtos.response.SizeResponseDto;
import com.openfashion.openfasion_marketplace.services.SizeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/size")
public class SizeController {

    private final SizeService sizeService;

    public SizeController(SizeService sizeService) {
        this.sizeService = sizeService;
    }

    @PostMapping("/add")
    public String addSize(@RequestBody String size) {
        return sizeService.addSize(size);
    }

//    @GetMapping("/{id}")
//    public SizeResponseDto getSize(@PathVariable Long id) {
//        return sizeService.getSizeById(id);
//    }
//
//    @GetMapping("/{name}")
//    public SizeResponseDto getSizeByName(@PathVariable String name) {
//        return sizeService.getSizeByName(name);
//    }

    @GetMapping("/{value}")
    public SizeResponseDto getSize(@PathVariable String value) {
        long id;
        try {
            id = Long.parseLong(value);
        } catch (NumberFormatException e) {
            id = 0;
            System.out.println(e.getMessage());
        }


        if(id > 0) {
            return sizeService.getSizeById(Long.parseLong(value));
        } else {
            return sizeService.getSizeByName(value);
        }
    }

    @GetMapping("/all")
    public List<SizeResponseDto> getAllSize() {
        return sizeService.getAllSizes();
    }

    @DeleteMapping("/{id}")
    public String deleteSize(@PathVariable Long id) {
        return sizeService.deleteSize(id);
    }
}
