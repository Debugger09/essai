package com.ime.api.fichier.service.impl;

import com.ime.api.fichier.dto.FichierDto;
import com.ime.api.fichier.mapper.FichierMapper;
import com.ime.api.fichier.model.Fichier;
import com.ime.api.fichier.repository.FichierRepository;
import com.ime.api.fichier.service.FichierService;
import com.ime.api.projet.model.Projet;
import com.ime.api.projet.repository.ProjetRepository;
import com.ime.api.tache.model.Tache;
import com.ime.api.tache.repository.TacheRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FichierServiceImpl implements FichierService {

    private final FichierRepository fichierRepository;
    private final FichierMapper fichierMapper;
    private final ProjetRepository projetRepository;
    private final TacheRepository tacheRepository;

    @Override
    public FichierDto createFichier(FichierDto fichierDto) {
        Fichier fichier = fichierMapper.toEntity(fichierDto);

        // Récupérer le projet complet si présent
        if (fichierDto.getProjet() != null && fichierDto.getProjet().getId() != null) {
            Projet projet = projetRepository.findById(fichierDto.getProjet().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Projet introuvable"));
            fichier.setProjet(projet);
        } else {
            fichier.setProjet(null);
        }

        // Récupérer la tâche complète si présente
        if (fichierDto.getTache() != null && fichierDto.getTache().getId() != null) {
            Tache tache = tacheRepository.findById(fichierDto.getTache().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable"));
            fichier.setTache(tache);
        } else {
            fichier.setTache(null);
        }

        Fichier saved = fichierRepository.save(fichier);
        return fichierMapper.toDto(saved);
    }

    @Override
    public FichierDto getFichierById(Long id) {
        Fichier fichier = fichierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fichier introuvable avec l'id : " + id));
        return fichierMapper.toDto(fichier);
    }

    @Override
    public List<FichierDto> getAllFichiers() {
        return fichierRepository.findAll()
                .stream()
                .map(fichierMapper::toDto)
                .toList();
    }

    @Override
    public FichierDto updateFichier(Long id, FichierDto fichierDto) {
        Fichier fichier = fichierRepository.findById(id)
                .map(f -> {
                    f.setNom(fichierDto.getNom());
                    f.setChemin(fichierDto.getChemin());
                    f.setType(fichierDto.getType());
                    f.setTaille(fichierDto.getTaille());
                    // Mettre à jour le projet
                    if (fichierDto.getProjet() != null && fichierDto.getProjet().getId() != null) {
                        Projet projet = projetRepository.findById(fichierDto.getProjet().getId())
                                .orElseThrow(() -> new EntityNotFoundException("Projet introuvable"));
                        f.setProjet(projet);
                    } else {
                        f.setProjet(null);
                    }
                    // Mettre à jour la tâche
                    if (fichierDto.getTache() != null && fichierDto.getTache().getId() != null) {
                        Tache tache = tacheRepository.findById(fichierDto.getTache().getId())
                                .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable"));
                        f.setTache(tache);
                    } else {
                        f.setTache(null);
                    }
                    return fichierRepository.save(f);
                })
                .orElseThrow(() -> new EntityNotFoundException("Fichier introuvable avec l'id : " + id));
        return fichierMapper.toDto(fichier);
    }

    @Override
    public void deleteFichier(Long id) {
        fichierRepository.deleteById(id);
    }
}