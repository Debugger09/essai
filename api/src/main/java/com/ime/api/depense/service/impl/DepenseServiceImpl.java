package com.ime.api.depense.service.impl;

import com.ime.api.depense.dto.DepenseDto;
import com.ime.api.depense.mapper.DepenseMapper;
import com.ime.api.depense.model.Depense;
import com.ime.api.depense.repository.DepenseRepository;
import com.ime.api.depense.service.DepenseService;
import com.ime.api.projet.model.Projet;
import com.ime.api.projet.repository.ProjetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepenseServiceImpl implements DepenseService {

    private final DepenseRepository depenseRepository;
    private final DepenseMapper depenseMapper;
    private final ProjetRepository projetRepository;

    @Override
    public DepenseDto createDepense(DepenseDto depenseDto) {
        Depense depense = depenseMapper.toEntity(depenseDto);

        // Récupérer le projet complet si présent
        if (depenseDto.getProjetId() != null) {
            Projet projet = projetRepository.findById(depenseDto.getProjetId())
                    .orElseThrow(() -> new EntityNotFoundException("Projet introuvable"));
            depense.setProjet(projet);
        } else {
            depense.setProjet(null);
        }

        Depense saved = depenseRepository.save(depense);
        return depenseMapper.toDto(saved);
    }

    @Override
    public DepenseDto getDepenseById(Long id) {
        Depense depense = depenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dépense introuvable avec l'id : " + id));
        return depenseMapper.toDto(depense);
    }

    @Override
    public List<DepenseDto> getAllDepenses() {
        return depenseRepository.findAll()
                .stream()
                .map(depenseMapper::toDto)
                .toList();
    }

    @Override
    public DepenseDto updateDepense(Long id, DepenseDto depenseDto) {
        Depense depense = depenseRepository.findById(id)
                .map(d -> {
                    d.setLibelle(depenseDto.getLibelle());
                    d.setMontant(depenseDto.getMontant());
                    // Mettre à jour le projet
                    if (depenseDto.getProjetId() != null) {
                        Projet projet = projetRepository.findById(depenseDto.getProjetId())
                                .orElseThrow(() -> new EntityNotFoundException("Projet introuvable"));
                        d.setProjet(projet);
                    } else {
                        d.setProjet(null);
                    }
                    return depenseRepository.save(d);
                })
                .orElseThrow(() -> new EntityNotFoundException("Dépense introuvable avec l'id : " + id));
        return depenseMapper.toDto(depense);
    }

    @Override
    public void deleteDepense(Long id) {
        depenseRepository.deleteById(id);
    }
}