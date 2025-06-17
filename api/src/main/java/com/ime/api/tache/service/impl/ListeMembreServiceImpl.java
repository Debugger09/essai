package com.ime.api.tache.service.impl;

import com.ime.api.tache.dto.ListeMembreDto;
import com.ime.api.tache.mapper.ListeMembreMapper;
import com.ime.api.tache.model.ListeMembre;
import com.ime.api.tache.repository.ListeMembreRepository;
import com.ime.api.tache.service.ListeMembreService;
import com.ime.api.tache.model.Tache;
import com.ime.api.tache.repository.TacheRepository;
import com.ime.api.user.model.User;
import com.ime.api.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListeMembreServiceImpl implements ListeMembreService {

    private final ListeMembreRepository listeMembreRepository;
    private final ListeMembreMapper listeMembreMapper;
    private final UserRepository userRepository;
    private final TacheRepository tacheRepository;

    @Override
    public ListeMembreDto createListeMembre(ListeMembreDto dto) {
        ListeMembre entity = listeMembreMapper.toEntity(dto);

        // Récupérer le membre complet
        if (dto.getMembre() != null && dto.getMembre().getId() != null) {
            User membre = userRepository.findById(dto.getMembre().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Membre introuvable"));
            entity.setMembre(membre);
        } else {
            entity.setMembre(null);
        }

        // Récupérer la tâche complète
        if (dto.getTache() != null && dto.getTache().getId() != null) {
            Tache tache = tacheRepository.findById(dto.getTache().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable"));
            entity.setTache(tache);
        } else {
            entity.setTache(null);
        }

        ListeMembre saved = listeMembreRepository.save(entity);
        return listeMembreMapper.toDto(saved);
    }

    @Override
    public ListeMembreDto getListeMembreById(Long id) {
        ListeMembre entity = listeMembreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ListeMembre introuvable avec l'id : " + id));
        return listeMembreMapper.toDto(entity);
    }

    @Override
    public List<ListeMembreDto> getAllListeMembres() {
        return listeMembreRepository.findAll()
                .stream()
                .map(listeMembreMapper::toDto)
                .toList();
    }

    @Override
    public ListeMembreDto updateListeMembre(Long id, ListeMembreDto dto) {
        ListeMembre entity = listeMembreRepository.findById(id)
                .map(lm -> {
                    // Mettre à jour le membre
                    if (dto.getMembre() != null && dto.getMembre().getId() != null) {
                        User membre = userRepository.findById(dto.getMembre().getId())
                                .orElseThrow(() -> new EntityNotFoundException("Membre introuvable"));
                        lm.setMembre(membre);
                    } else {
                        lm.setMembre(null);
                    }
                    // Mettre à jour la tâche
                    if (dto.getTache() != null && dto.getTache().getId() != null) {
                        Tache tache = tacheRepository.findById(dto.getTache().getId())
                                .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable"));
                        lm.setTache(tache);
                    } else {
                        lm.setTache(null);
                    }
                    return listeMembreRepository.save(lm);
                })
                .orElseThrow(() -> new EntityNotFoundException("ListeMembre introuvable avec l'id : " + id));
        return listeMembreMapper.toDto(entity);
    }

    @Override
    public void deleteListeMembre(Long id) {
        listeMembreRepository.deleteById(id);
    }
}