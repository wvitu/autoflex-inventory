package com.autoflex.inventory.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductionSuggestionResponse(
        List<ProductionSuggestionItemResponse> items,
        BigDecimal totalValue,
        List<RawMaterialStockResponse> remainingStock
) {}
