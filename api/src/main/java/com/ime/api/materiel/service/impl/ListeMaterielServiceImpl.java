package com.ime.api.materiel.service.impl;

import com.ime.api.materiel.dto.ListeMaterielDto;
import com.ime.api.materiel.mapper.ListeMaterielMapper;
import com.ime.api.materiel.model.ListeMateriel;
import com.ime.api.materiel.model.Materiel;
import com.ime.api.materiel.repository.ListeMaterielRepository;
import com.ime.api.materiel.repository.MaterielRepository;
import com.ime.api.materiel.service.ListeMaterielService;
import com.ime.api.tache.model.Tache;
import com.ime.api.tache.repository.TacheRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ListeMaterielServiceImpl implements ListeMaterielService {

    private static final Logger log = LoggerFactory.getLogger(ListeMaterielServiceImpl.class);
    private final ListeMaterielRepository listeMaterielRepository;
    private final ListeMaterielMapper listeMaterielMapper;
    private final MaterielRepository materielRepository;
    private final TacheRepository tacheRepository;

    @Override
    public ListeMaterielDto createListeMateriel(ListeMaterielDto dto) {
        ListeMateriel entity = listeMaterielMapper.toEntity(dto);

        // Récupérer le matériel complet
        Materiel materiel = null;
        if (dto.getMaterielId() != null) {
            materiel = materielRepository.findById(dto.getMaterielId())
                    .orElseThrow(() -> new EntityNotFoundException("Matériel introuvable"));
            entity.setMateriel(materiel);
        } else if (dto.getMateriel() != null && dto.getMateriel().getId() != null) {
            materiel = materielRepository.findById(dto.getMateriel().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Matériel introuvable"));
            entity.setMateriel(materiel);
        } else {
            entity.setMateriel(null);
        }

        // Récupérer la tâche complète
        if (dto.getTacheId() != null) {
            Tache tache = tacheRepository.findById(dto.getTacheId())
                    .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable"));
            entity.setTache(tache);
        } else if (dto.getTache() != null && dto.getTache().getId() != null) {
            Tache tache = tacheRepository.findById(dto.getTache().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable"));
            entity.setTache(tache);
        } else {
            entity.setTache(null);
        }

        entity.setQuantite(dto.getQuantite());

        // Gestion du stock : décrémenter la quantité disponible
        if (materiel != null && dto.getQuantite() != null) {
            if (materiel.getQuantite() == null || materiel.getQuantite() < dto.getQuantite()) {
                log.warn("Tentative d'assignation avec stock insuffisant pour le matériel {} (stock: {}, demandé: {})", materiel.getId(), materiel.getQuantite(), dto.getQuantite());
                throw new IllegalArgumentException("Stock insuffisant pour ce matériel");
            }
            materiel.setQuantite(materiel.getQuantite() - dto.getQuantite());
            materielRepository.save(materiel);
            log.info("Matériel {} assigné à une tâche. Stock restant: {}", materiel.getId(), materiel.getQuantite());
        }

        ListeMateriel saved = listeMaterielRepository.save(entity);
        return listeMaterielMapper.toDto(saved);
    }

    @Override
    public ListeMaterielDto getListeMaterielById(Long id) {
        ListeMateriel entity = listeMaterielRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ListeMateriel introuvable avec l'id : " + id));
        return listeMaterielMapper.toDto(entity);
    }

    @Override
    public List<ListeMaterielDto> getAllListeMateriels() {
        return listeMaterielRepository.findAll()
                .stream()
                .map(listeMaterielMapper::toDto)
                .toList();
    }

    @Override
    public ListeMaterielDto updateListeMateriel(Long id, ListeMaterielDto dto) {
        ListeMateriel entity = listeMaterielRepository.findById(id)
                .map(lm -> {
                    lm.setQuantite(dto.getQuantite());
                    // Mettre à jour le matériel
                    if (dto.getMateriel() != null && dto.getMateriel().getId() != null) {
                        Materiel materiel = materielRepository.findById(dto.getMateriel().getId())
                                .orElseThrow(() -> new EntityNotFoundException("Matériel introuvable"));
                        lm.setMateriel(materiel);
                    } else {
                        lm.setMateriel(null);
                    }
                    // Mettre à jour la tâche
                    if (dto.getTache() != null && dto.getTache().getId() != null) {
                        Tache tache = tacheRepository.findById(dto.getTache().getId())
                                .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable"));
                        lm.setTache(tache);
                    } else {
                        lm.setTache(null);
                    }
                    return listeMaterielRepository.save(lm);
                })
                .orElseThrow(() -> new EntityNotFoundException("ListeMateriel introuvable avec l'id : " + id));
        return listeMaterielMapper.toDto(entity);
    }

    @Override
    public void deleteListeMateriel(Long id) {
        listeMaterielRepository.deleteById(id);
    }

    // Méthode de restitution de matériel réutilisable
    public void returnListeMateriel(Long id) {
        ListeMateriel entity = listeMaterielRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ListeMateriel introuvable avec l'id : " + id));
        Materiel materiel = entity.getMateriel();
        if (materiel != null && materiel.isReutilisable() && entity.getQuantite() != null) {
            materiel.setQuantite((materiel.getQuantite() == null ? 0 : materiel.getQuantite()) + entity.getQuantite());
            materielRepository.save(materiel);
            log.info("Matériel {} restitué. Nouveau stock: {}", materiel.getId(), materiel.getQuantite());
        }
        listeMaterielRepository.deleteById(id);
        log.info("Affectation ListeMateriel {} supprimée.", id);
    }
}