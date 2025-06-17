package com.ime.api.tache.controller;

import com.ime.api.tache.dto.ListeMembreDto;
import com.ime.api.tache.service.ListeMembreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listemembres")
@RequiredArgsConstructor
public class ListeMembreController {

    private final ListeMembreService listeMembreService;

    @PostMapping
    public ListeMembreDto create(@RequestBody ListeMembreDto dto) {
        return listeMembreService.createListeMembre(dto);
    }

    @GetMapping("/{id}")
    public ListeMembreDto getById(@PathVariable Long id) {
        return listeMembreService.getListeMembreById(id);
    }

    @GetMapping
    public List<ListeMembreDto> getAll() {
        return listeMembreService.getAllListeMembres();
    }

    @PutMapping("/{id}")
    public ListeMembreDto update(@PathVariable Long id, @RequestBody ListeMembreDto dto) {
        return listeMembreService.updateListeMembre(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        listeMembreService.deleteListeMembre(id);
    }
}