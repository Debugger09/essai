package com.ime.api.projet.service.impl;

import com.ime.api.projet.dto.ProjetDto;
import com.ime.api.projet.mapper.ProjetMapper;
import com.ime.api.projet.model.Projet;
import com.ime.api.projet.repository.ProjetRepository;
import com.ime.api.projet.service.ProjetService;
import com.ime.api.user.model.User;
import com.ime.api.user.repository.UserRepository;
import com.ime.api.tache.service.impl.TacheServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjetServiceImpl implements ProjetService {

    private final ProjetRepository projetRepository;
    private final ProjetMapper projetMapper;
    private final UserRepository userRepository;
    private final TacheServiceImpl tacheServiceImpl;

    @Override
    public ProjetDto createProjet(ProjetDto projetDto) {
        Projet projet = projetMapper.toEntity(projetDto);

        // Récupérer le chef de projet complet si présent
        if (projetDto.getChefProjet() != null && projetDto.getChefProjet().getId() != null) {
            User chef = userRepository.findById(projetDto.getChefProjet().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Chef de projet introuvable"));
            projet.setChefProjet(chef);
        } else {
            projet.setChefProjet(null);
        }

        Projet savedProjet = projetRepository.save(projet);
        return projetMapper.toDto(savedProjet);
    }

    @Override
    public ProjetDto getProjetById(Long id) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projet not found with id: " + id));
        ProjetDto projetDto = projetMapper.toDto(projet);
        // Enrichir chaque tâche avec ses membres
        if (projetDto.getTaches() != null) {
            for (int i = 0; i < projetDto.getTaches().size(); i++) {
                var tacheDto = projetDto.getTaches().get(i);
                var tache = projet.getTaches().stream()
                    .filter(t -> t.getId().equals(tacheDto.getId()))
                    .findFirst().orElse(null);
                if (tache != null) {
                    tacheServiceImpl.enrichirMembres(tache, tacheDto);
                }
            }
        }
        return projetDto;
    }

    @Override
    public List<ProjetDto> getAllProjets() {
        return projetRepository.findAll()
                .stream()
                .map(projet -> {
                    ProjetDto projetDto = projetMapper.toDto(projet);
                    if (projetDto.getTaches() != null) {
                        for (int i = 0; i < projetDto.getTaches().size(); i++) {
                            var tacheDto = projetDto.getTaches().get(i);
                            var tache = projet.getTaches().stream()
                                .filter(t -> t.getId().equals(tacheDto.getId()))
                                .findFirst().orElse(null);
                            if (tache != null) {
                                tacheServiceImpl.enrichirMembres(tache, tacheDto);
                            }
                        }
                    }
                    return projetDto;
                })
                .toList();
    }

    @Override
    public ProjetDto updateProjet(Long id, ProjetDto projetDto) {
        Projet projet = projetRepository.findById(id)
                .map(p -> {
                    p.setName(projetDto.getName());
                    p.setDateDebut(projetDto.getDateDebut());
                    p.setDateFin(projetDto.getDateFin());
                    p.setDescription(projetDto.getDescription());
                    p.setStatus(projetDto.getStatus());
                    p.setBudget(projetDto.getBudget());
                    // Charger le chef de projet complet
                    if (projetDto.getChefProjet() != null && projetDto.getChefProjet().getId() != null) {
                        User chef = userRepository.findById(projetDto.getChefProjet().getId())
                                .orElseThrow(() -> new EntityNotFoundException("Chef de projet introuvable"));
                        p.setChefProjet(chef);
                    } else {
                        p.setChefProjet(null);
                    }
                    return projetRepository.save(p);
                })
                .orElseThrow(() -> new EntityNotFoundException("Projet not found with id: " + id));
        return projetMapper.toDto(projet);
    }

    @Override
    public void deleteProjet(Long id) {
        projetRepository.deleteById(id);
    }
}