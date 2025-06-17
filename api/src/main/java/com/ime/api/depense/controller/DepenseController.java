package com.ime.api.depense.controller;

import com.ime.api.depense.dto.DepenseDto;
import com.ime.api.depense.service.DepenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/depenses")
@RequiredArgsConstructor
public class DepenseController {

    private final DepenseService depenseService;

    @PostMapping
    public DepenseDto create(@RequestBody DepenseDto dto) {
        return depenseService.createDepense(dto);
    }

    @GetMapping("/{id}")
    public DepenseDto getById(@PathVariable Long id) {
        return depenseService.getDepenseById(id);
    }

    @GetMapping
    public List<DepenseDto> getAll() {
        return depenseService.getAllDepenses();
    }

    @PutMapping("/{id}")
    public DepenseDto update(@PathVariable Long id, @RequestBody DepenseDto dto) {
        return depenseService.updateDepense(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        depenseService.deleteDepense(id);
    }
}