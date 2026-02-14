package com.autoflex.inventory.service;

import com.autoflex.inventory.domain.*;
import com.autoflex.inventory.dto.*;
import com.autoflex.inventory.exception.BusinessException;
import com.autoflex.inventory.exception.ResourceNotFoundException;
import com.autoflex.inventory.repository.ProductRawMaterialRepository;
import com.autoflex.inventory.repository.ProductRepository;
import com.autoflex.inventory.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillOfMaterialsService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final ProductRawMaterialRepository productRawMaterialRepository;

    @Transactional(readOnly = true)
    public List<BillOfMaterialsItemResponse> getByProduct(Long productId) {
        ensureProductExists(productId);

        return productRawMaterialRepository.findByProduct_Id(productId)
                .stream()
                .map(prm -> new BillOfMaterialsItemResponse(
                        prm.getRawMaterial().getId(),
                        prm.getRawMaterial().getCode(),
                        prm.getRawMaterial().getName(),
                        prm.getRequiredQuantity()
                ))
                .toList();
    }

    @Transactional
    public List<BillOfMaterialsItemResponse> replace(Long productId, List<BillOfMaterialsItemRequest> items) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: id=" + productId));

        if (items == null) items = List.of();

        Set<Long> ids = new HashSet<>();
        for (BillOfMaterialsItemRequest item : items) {
            if (!ids.add(item.rawMaterialId())) {
                throw new BusinessException("Duplicate rawMaterialId in request: " + item.rawMaterialId());
            }
        }

        List<Long> rawMaterialIds = items.stream().map(BillOfMaterialsItemRequest::rawMaterialId).toList();
        Map<Long, RawMaterial> rawMaterialMap = rawMaterialRepository.findAllById(rawMaterialIds)
                .stream()
                .collect(Collectors.toMap(RawMaterial::getId, rm -> rm));

        if (rawMaterialMap.size() != rawMaterialIds.size()) {
            for (Long id : rawMaterialIds) {
                if (!rawMaterialMap.containsKey(id)) {
                    throw new ResourceNotFoundException("Raw material not found: id=" + id);
                }
            }
        }

        productRawMaterialRepository.deleteByProduct_Id(productId);

        List<ProductRawMaterial> toSave = new ArrayList<>();
        for (BillOfMaterialsItemRequest item : items) {
            RawMaterial rm = rawMaterialMap.get(item.rawMaterialId());

            ProductRawMaterial prm = ProductRawMaterial.builder()
                    .id(new ProductRawMaterialId(product.getId(), rm.getId()))
                    .product(product)
                    .rawMaterial(rm)
                    .requiredQuantity(item.requiredQuantity())
                    .build();

            toSave.add(prm);
        }

        productRawMaterialRepository.saveAll(toSave);

        return getByProduct(productId);
    }

    private void ensureProductExists(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found: id=" + productId);
        }
    }
}
