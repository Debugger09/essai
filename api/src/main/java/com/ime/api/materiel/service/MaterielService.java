package com.ime.api.materiel.service;

import com.ime.api.materiel.dto.MaterielDto;
import java.util.List;

public interface MaterielService {
    MaterielDto createMateriel(MaterielDto materielDto);
    MaterielDto getMaterielById(Long id);
    List<MaterielDto> getAllMateriels();
    List<MaterielDto> getMaterielsDisponibles();
    MaterielDto updateMateriel(Long id, MaterielDto materielDto);
    void deleteMateriel(Long id);
}