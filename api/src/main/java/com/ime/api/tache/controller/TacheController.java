package com.ime.api.tache.controller;

import com.ime.api.tache.dto.TacheDto;
import com.ime.api.tache.service.TacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taches")
@RequiredArgsConstructor
public class TacheController {

    private final TacheService tacheService;

    @PostMapping
    public TacheDto create(@RequestBody TacheDto tacheDto) {
        return tacheService.createTache(tacheDto);
    }

    @GetMapping("/{id}")
    public TacheDto getById(@PathVariable Long id) {
        return tacheService.getTacheById(id);
    }

    @GetMapping
    public List<TacheDto> getAll() {
        return tacheService.getAllTaches();
    }

    @PutMapping("/{id}")
    public TacheDto update(@PathVariable Long id, @RequestBody TacheDto tacheDto) {
        return tacheService.updateTache(id, tacheDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tacheService.deleteTache(id);
    }
}