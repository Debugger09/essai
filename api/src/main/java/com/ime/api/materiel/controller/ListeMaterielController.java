package com.ime.api.materiel.controller;

import com.ime.api.materiel.dto.ListeMaterielDto;
import com.ime.api.materiel.service.ListeMaterielService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listemateriels")
@RequiredArgsConstructor
public class ListeMaterielController {

    private final ListeMaterielService listeMaterielService;

    @PostMapping
    public ListeMaterielDto create(@RequestBody ListeMaterielDto dto) {
        return listeMaterielService.createListeMateriel(dto);
    }

    @GetMapping("/{id}")
    public ListeMaterielDto getById(@PathVariable Long id) {
        return listeMaterielService.getListeMaterielById(id);
    }

    @GetMapping
    public List<ListeMaterielDto> getAll() {
        return listeMaterielService.getAllListeMateriels();
    }

    @PutMapping("/{id}")
    public ListeMaterielDto update(@PathVariable Long id, @RequestBody ListeMaterielDto dto) {
        return listeMaterielService.updateListeMateriel(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        listeMaterielService.deleteListeMateriel(id);
    }

    @PostMapping("/{id}/return")
    public void returnMateriel(@PathVariable Long id) {
        listeMaterielService.returnListeMateriel(id);
    }
}