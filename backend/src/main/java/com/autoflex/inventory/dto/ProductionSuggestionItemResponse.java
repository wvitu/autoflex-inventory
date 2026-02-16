package com.autoflex.inventory.dto;

import java.math.BigDecimal;

public record ProductionSuggestionItemResponse(
        Long productId,
        String productCode,
        String productName,
        BigDecimal unitPrice,
        int producibleQuantity,
        BigDecimal totalValue
) {}
