package com.autoflex.inventory.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ProductRawMaterialId implements Serializable {

    private Long productId;
    private Long rawMaterialId;
}
