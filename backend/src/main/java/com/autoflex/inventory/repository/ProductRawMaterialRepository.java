package com.autoflex.inventory.repository;

import com.autoflex.inventory.domain.ProductRawMaterial;
import com.autoflex.inventory.domain.ProductRawMaterialId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRawMaterialRepository extends JpaRepository<ProductRawMaterial, ProductRawMaterialId> {
    List<ProductRawMaterial> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}
