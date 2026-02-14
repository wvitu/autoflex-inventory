package com.autoflex.inventory.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "raw_materials", uniqueConstraints = {
        @UniqueConstraint(name = "uk_raw_materials_code", columnNames = "code")
})
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String code;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String name;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 12, fraction = 3)
    @Column(name = "stock_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal stockQuantity;
}
