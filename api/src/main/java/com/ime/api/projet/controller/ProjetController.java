package com.ime.api.projet.controller;

import com.ime.api.projet.dto.ProjetDto;
import com.ime.api.projet.service.ProjetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
@RequiredArgsConstructor
public class ProjetController {

    private final ProjetService projetService;

    @PostMapping
    public ProjetDto create(@RequestBody ProjetDto projetDto) {
        return projetService.createProjet(projetDto);
    }

    @GetMapping("/{id}")
    public ProjetDto getById(@PathVariable Long id) {
        return projetService.getProjetById(id);
    }

    @GetMapping
    public List<ProjetDto> getAll() {
        return projetService.getAllProjets();
    }

    @PutMapping("/{id}")
    public ProjetDto update(@PathVariable Long id, @RequestBody ProjetDto projetDto) {
        return projetService.updateProjet(id, projetDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        projetService.deleteProjet(id);
    }
}