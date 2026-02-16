package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.ProductionSuggestionResponse;
import com.autoflex.inventory.service.ProductionSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/production")
public class ProductionSuggestionController {

    private final ProductionSuggestionService productionSuggestionService;

    @GetMapping("/suggestion")
    public ProductionSuggestionResponse suggest() {
        return productionSuggestionService.suggest();
    }
}
