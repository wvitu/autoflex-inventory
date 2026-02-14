package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.*;
import com.autoflex.inventory.service.RawMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/raw-materials")
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @GetMapping
    public List<RawMaterialResponse> list() {
        return rawMaterialService.list();
    }

    @GetMapping("/{id}")
    public RawMaterialResponse get(@PathVariable Long id) {
        return rawMaterialService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RawMaterialResponse create(@RequestBody @Valid RawMaterialCreateRequest request) {
        return rawMaterialService.create(request);
    }

    @PutMapping("/{id}")
    public RawMaterialResponse update(@PathVariable Long id,
                                      @RequestBody @Valid RawMaterialUpdateRequest request) {
        return rawMaterialService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rawMaterialService.delete(id);
    }
}
