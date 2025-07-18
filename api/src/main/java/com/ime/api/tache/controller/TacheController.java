package com.ime.api.tache.controller;

import com.ime.api.tache.dto.TacheDto;
import com.ime.api.tache.service.TacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.ime.api.user.service.UserService;

@RestController
@RequestMapping("/api/taches")
@RequiredArgsConstructor
public class TacheController {

    private final TacheService tacheService;
    private final UserService userService;

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

    @PutMapping("/{id}/statut")
    public ResponseEntity<?> changerStatutTache(
        @PathVariable Long id,
        @RequestBody Map<String, String> payload,
        Principal principal
    ) {
        String nouveauStatut = payload.get("statutTache");
        Long userId = userService.getUserByEmail(principal.getName()).getId();
        try {
            TacheDto updated = tacheService.changerStatutTache(id, nouveauStatut, userId);
            return ResponseEntity.ok(updated);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tacheService.deleteTache(id);
    }
}