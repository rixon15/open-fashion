package com.openfashion.openfasion_marketplace.services;

import com.openfashion.openfasion_marketplace.models.dtos.response.SizeResponseDto;
import com.openfashion.openfasion_marketplace.models.entities.Size;
import com.openfashion.openfasion_marketplace.models.mappers.SizeMapper;
import com.openfashion.openfasion_marketplace.repositories.SizeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeService {
    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;

    public SizeService(SizeRepository sizeRepository, SizeMapper sizeMapper) {
        this.sizeRepository = sizeRepository;
        this.sizeMapper = sizeMapper;
    }

    public String addSize(String size) {

        Optional<Size> existingSize = sizeRepository.findByName(size);

        if (existingSize.isPresent()) {
            return "Size already exists";
        }

        Size newSize = new Size();
        newSize.setName(size);
        sizeRepository.save(newSize);
        return "Size added successfully";
    }

    public SizeResponseDto getSizeById(Long id) {

        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Size not found"));

        return sizeMapper.toSizeResponseDto(size);

    }

    public SizeResponseDto getSizeByName(String name) {

        Size size = sizeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Size not found"));

        return sizeMapper.toSizeResponseDto(size);

    }

    public List<SizeResponseDto> getAllSizes() {

        List<Size> size = sizeRepository.findAll();

        return sizeMapper.toSizeResponseDtoList(size);

    }

    public String deleteSize(Long id) {

        Optional<Size> existingSize = sizeRepository.findById(id);

        if (existingSize.isEmpty()) {
            return "Size not found";
        }

        sizeRepository.deleteById(id);

        return "Size deleted successfully";
    }
}
