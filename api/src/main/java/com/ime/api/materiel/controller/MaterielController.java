package com.ime.api.materiel.controller;

import com.ime.api.materiel.dto.MaterielDto;
import com.ime.api.materiel.service.MaterielService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materiels")
@RequiredArgsConstructor
public class MaterielController {

    private final MaterielService materielService;

    @PostMapping
    public MaterielDto create(@RequestBody MaterielDto dto) {
        return materielService.createMateriel(dto);
    }

    @GetMapping("/{id}")
    public MaterielDto getById(@PathVariable Long id) {
        return materielService.getMaterielById(id);
    }

    @GetMapping
    public List<MaterielDto> getAll() {
        return materielService.getAllMateriels();
    }

    @PutMapping("/{id}")
    public MaterielDto update(@PathVariable Long id, @RequestBody MaterielDto dto) {
        return materielService.updateMateriel(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        materielService.deleteMateriel(id);
    }
}