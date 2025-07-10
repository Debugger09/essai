package com.ime.api.projet.service;

import com.ime.api.projet.dto.ProjetDto;
import com.ime.api.materiel.dto.ListeMaterielDto;
import java.util.List;

public interface ProjetService {
    ProjetDto createProjet(ProjetDto projetDto);
    ProjetDto getProjetById(Long id);
    List<ProjetDto> getAllProjets();
    ProjetDto updateProjet(Long id, ProjetDto projetDto);
    void deleteProjet(Long id);
    List<ListeMaterielDto> getMaterielsByProjet(Long projetId);
}