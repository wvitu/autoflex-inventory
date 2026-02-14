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
@Table(name = "product_raw_materials")
public class ProductRawMaterial {

    @EmbeddedId
    private ProductRawMaterialId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_prm_product"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("rawMaterialId")
    @JoinColumn(name = "raw_material_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_prm_raw_material"))
    private RawMaterial rawMaterial;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    @Digits(integer = 12, fraction = 3)
    @Column(name = "required_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal requiredQuantity;
}
