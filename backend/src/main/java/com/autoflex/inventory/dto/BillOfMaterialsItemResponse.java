package com.autoflex.inventory.dto;

import java.math.BigDecimal;

public record BillOfMaterialsItemResponse(
        Long rawMaterialId,
        String rawMaterialCode,
        String rawMaterialName,
        BigDecimal requiredQuantity
) {}
