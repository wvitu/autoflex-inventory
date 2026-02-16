package com.autoflex.inventory.dto;

import java.math.BigDecimal;

public record RawMaterialStockResponse(
        Long rawMaterialId,
        String code,
        String name,
        BigDecimal remainingQuantity
) {}
