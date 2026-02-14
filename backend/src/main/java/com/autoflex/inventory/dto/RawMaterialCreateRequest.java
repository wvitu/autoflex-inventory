package com.autoflex.inventory.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record RawMaterialCreateRequest(
        @NotBlank @Size(max = 50) String code,
        @NotBlank @Size(max = 120) String name,
        @NotNull @DecimalMin(value = "0.00", inclusive = true) @Digits(integer = 12, fraction = 3) BigDecimal stockQuantity
) {}
