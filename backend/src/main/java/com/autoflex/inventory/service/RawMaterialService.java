package com.autoflex.inventory.service;

import com.autoflex.inventory.domain.RawMaterial;
import com.autoflex.inventory.dto.*;
import com.autoflex.inventory.exception.BusinessException;
import com.autoflex.inventory.exception.ResourceNotFoundException;
import com.autoflex.inventory.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;

    @Transactional(readOnly = true)
    public List<RawMaterialResponse> list() {
        return rawMaterialRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RawMaterialResponse getById(Long id) {
        RawMaterial rm = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found: id=" + id));
        return toResponse(rm);
    }

    @Transactional
    public RawMaterialResponse create(RawMaterialCreateRequest request) {
        if (rawMaterialRepository.existsByCode(request.code())) {
            throw new BusinessException("Raw material code already exists: " + request.code());
        }

        RawMaterial rm = RawMaterial.builder()
                .code(request.code().trim())
                .name(request.name().trim())
                .stockQuantity(request.stockQuantity())
                .build();

        RawMaterial saved = rawMaterialRepository.save(rm);
        return toResponse(saved);
    }

    @Transactional
    public RawMaterialResponse update(Long id, RawMaterialUpdateRequest request) {
        RawMaterial rm = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found: id=" + id));

        rm.setName(request.name().trim());
        rm.setStockQuantity(request.stockQuantity());

        RawMaterial saved = rawMaterialRepository.save(rm);
        return toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!rawMaterialRepository.existsById(id)) {
            throw new ResourceNotFoundException("Raw material not found: id=" + id);
        }
        rawMaterialRepository.deleteById(id);
    }

    private RawMaterialResponse toResponse(RawMaterial rm) {
        return new RawMaterialResponse(rm.getId(), rm.getCode(), rm.getName(), rm.getStockQuantity());
    }
}
