package com.autoflex.inventory.service;

import com.autoflex.inventory.domain.Product;
import com.autoflex.inventory.domain.ProductRawMaterial;
import com.autoflex.inventory.domain.RawMaterial;
import com.autoflex.inventory.dto.ProductionSuggestionItemResponse;
import com.autoflex.inventory.dto.ProductionSuggestionResponse;
import com.autoflex.inventory.dto.RawMaterialStockResponse;
import com.autoflex.inventory.repository.ProductRawMaterialRepository;
import com.autoflex.inventory.repository.ProductRepository;
import com.autoflex.inventory.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductionSuggestionService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final ProductRawMaterialRepository productRawMaterialRepository;

    @Transactional(readOnly = true)
    public ProductionSuggestionResponse suggest() {

        // virtual stock snapshot (we will "consume" this map)
        Map<Long, BigDecimal> stock = new HashMap<>();
        for (RawMaterial rm : rawMaterialRepository.findAll()) {
            stock.put(rm.getId(), rm.getStockQuantity());
        }

        List<Product> products = productRepository.findAllByOrderByPriceDesc();

        List<ProductionSuggestionItemResponse> items = new ArrayList<>();
        BigDecimal grandTotal = BigDecimal.ZERO;

        for (Product product : products) {
            List<ProductRawMaterial> bom = productRawMaterialRepository.findByProduct_Id(product.getId());

            if (bom.isEmpty()) {
                continue;
            }

            int producible = calculateProducibleQuantity(bom, stock);

            if (producible <= 0) {
                continue;
            }

            consumeStock(bom, stock, producible);

            BigDecimal itemTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(producible))
                    .setScale(2, RoundingMode.HALF_UP);

            grandTotal = grandTotal.add(itemTotal);

            items.add(new ProductionSuggestionItemResponse(
                    product.getId(),
                    product.getCode(),
                    product.getName(),
                    product.getPrice(),
                    producible,
                    itemTotal
            ));
        }

        List<RawMaterialStockResponse> remainingStock = rawMaterialRepository.findAll().stream()
                .map(rm -> new RawMaterialStockResponse(
                        rm.getId(),
                        rm.getCode(),
                        rm.getName(),
                        stock.getOrDefault(rm.getId(), BigDecimal.ZERO)
                ))
                .toList();

        return new ProductionSuggestionResponse(
                items,
                grandTotal.setScale(2, RoundingMode.HALF_UP),
                remainingStock
        );
    }

    private int calculateProducibleQuantity(List<ProductRawMaterial> bom, Map<Long, BigDecimal> stock) {
        int max = Integer.MAX_VALUE;

        for (ProductRawMaterial prm : bom) {
            Long rmId = prm.getRawMaterial().getId();
            BigDecimal available = stock.getOrDefault(rmId, BigDecimal.ZERO);
            BigDecimal required = prm.getRequiredQuantity();

            if (required == null || required.compareTo(BigDecimal.ZERO) <= 0) {
                return 0;
            }

            int possible = available.divide(required, 0, RoundingMode.FLOOR).intValue();
            max = Math.min(max, possible);
        }

        return max;
    }

    private void consumeStock(List<ProductRawMaterial> bom, Map<Long, BigDecimal> stock, int qty) {
        for (ProductRawMaterial prm : bom) {
            Long rmId = prm.getRawMaterial().getId();
            BigDecimal available = stock.getOrDefault(rmId, BigDecimal.ZERO);
            BigDecimal consumption = prm.getRequiredQuantity().multiply(BigDecimal.valueOf(qty));
            stock.put(rmId, available.subtract(consumption));
        }
    }
}
