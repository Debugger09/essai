package com.ime.api.projet.controller;

import com.ime.api.projet.dto.ProjetDto;
import com.ime.api.projet.service.ProjetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.ime.api.user.dto.UserDTO;
import com.ime.api.tache.model.Tache;
import com.ime.api.tache.model.ListeMembre;
import com.ime.api.tache.repository.ListeMembreRepository;
import com.ime.api.projet.repository.ProjetRepository;
import com.ime.api.user.mapper.UserMapper;

@RestController
@RequestMapping("/api/projets")
@RequiredArgsConstructor
public class ProjetController {

    private final ProjetService projetService;
    private final ProjetRepository projetRepository;
    private final ListeMembreRepository listeMembreRepository;
    private final UserMapper userMapper;

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

    @GetMapping("/{id}/membres")
    public List<UserDTO> getMembresByProjet(@PathVariable Long id) {
        // Récupère toutes les tâches du projet
        var projet = projetRepository.findById(id).orElseThrow();
        var tacheIds = projet.getTaches().stream().map(Tache::getId).toList();
        // Récupère tous les ListeMembre liés à ces tâches
        var membres = listeMembreRepository.findAll().stream()
            .filter(lm -> lm.getTache() != null && tacheIds.contains(lm.getTache().getId()))
            .map(ListeMembre::getMembre)
            .distinct()
            .map(userMapper::toDto)
            .toList();
        return membres;
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