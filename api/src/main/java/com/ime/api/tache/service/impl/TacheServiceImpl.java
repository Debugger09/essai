package com.ime.api.tache.service.impl;

import com.ime.api.tache.dto.TacheDto;
import com.ime.api.tache.mapper.TacheMapper;
import com.ime.api.tache.model.Tache;
import com.ime.api.tache.repository.TacheRepository;
import com.ime.api.tache.service.TacheService;
import com.ime.api.projet.model.Projet;
import com.ime.api.projet.repository.ProjetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TacheServiceImpl implements TacheService {

    private final TacheRepository tacheRepository;
    private final TacheMapper tacheMapper;
    private final ProjetRepository projetRepository;

    @Override
    public TacheDto createTache(TacheDto tacheDto) {
        Tache tache = tacheMapper.toEntity(tacheDto);

        // Récupérer le projet complet si présent
        if (tacheDto.getProjet() != null && tacheDto.getProjet().getId() != null) {
            Projet projet = projetRepository.findById(tacheDto.getProjet().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Projet introuvable"));
            tache.setProjet(projet);
        } else {
            tache.setProjet(null);
        }

        Tache savedTache = tacheRepository.save(tache);
        return tacheMapper.toDto(savedTache);
    }

    @Override
    public TacheDto getTacheById(Long id) {
        Tache tache = tacheRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable avec l'id : " + id));
        return tacheMapper.toDto(tache);
    }

    @Override
    public List<TacheDto> getAllTaches() {
        return tacheRepository.findAll()
                .stream()
                .map(tacheMapper::toDto)
                .toList();
    }

    @Override
    public TacheDto updateTache(Long id, TacheDto tacheDto) {
        Tache tache = tacheRepository.findById(id)
                .map(t -> {
                    t.setLibelle(tacheDto.getLibelle());
                    t.setDescription(tacheDto.getDescription());
                    t.setDateEcheance(tacheDto.getDateEcheance());
                    t.setPriorite(tacheDto.getPriorite());
                    t.setStatutTache(tacheDto.getStatutTache());
                    // Charger le projet complet
                    if (tacheDto.getProjet() != null && tacheDto.getProjet().getId() != null) {
                        Projet projet = projetRepository.findById(tacheDto.getProjet().getId())
                                .orElseThrow(() -> new EntityNotFoundException("Projet introuvable"));
                        t.setProjet(projet);
                    } else {
                        t.setProjet(null);
                    }
                    return tacheRepository.save(t);
                })
                .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable avec l'id : " + id));
        return tacheMapper.toDto(tache);
    }

    @Override
    public void deleteTache(Long id) {
        tacheRepository.deleteById(id);
    }
}