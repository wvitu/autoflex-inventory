package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.*;
import com.autoflex.inventory.service.BillOfMaterialsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/{productId}/bill-of-materials")
public class BillOfMaterialsController {

    private final BillOfMaterialsService billOfMaterialsService;

    @GetMapping
    public List<BillOfMaterialsItemResponse> get(@PathVariable Long productId) {
        return billOfMaterialsService.getByProduct(productId);
    }

    @PutMapping
    public List<BillOfMaterialsItemResponse> replace(@PathVariable Long productId,
                                                     @RequestBody @Valid List<BillOfMaterialsItemRequest> items) {
        return billOfMaterialsService.replace(productId, items);
    }
}
