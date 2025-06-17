package com.ime.api.fichier.controller;

import com.ime.api.fichier.dto.FichierDto;
import com.ime.api.fichier.service.FichierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fichiers")
@RequiredArgsConstructor
public class FichierController {

    private final FichierService fichierService;

    @PostMapping
    public FichierDto create(@RequestBody FichierDto dto) {
        return fichierService.createFichier(dto);
    }

    @GetMapping("/{id}")
    public FichierDto getById(@PathVariable Long id) {
        return fichierService.getFichierById(id);
    }

    @GetMapping
    public List<FichierDto> getAll() {
        return fichierService.getAllFichiers();
    }

    @PutMapping("/{id}")
    public FichierDto update(@PathVariable Long id, @RequestBody FichierDto dto) {
        return fichierService.updateFichier(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        fichierService.deleteFichier(id);
    }
}