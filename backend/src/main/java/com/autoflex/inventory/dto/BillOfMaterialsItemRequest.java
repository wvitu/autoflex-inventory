package com.autoflex.inventory.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record BillOfMaterialsItemRequest(
        @NotNull Long rawMaterialId,
        @NotNull
        @DecimalMin(value = "0.00", inclusive = false)
        @Digits(integer = 12, fraction = 3)
        BigDecimal requiredQuantity
) {}
