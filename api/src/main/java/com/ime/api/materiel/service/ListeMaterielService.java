package com.ime.api.materiel.service;

import com.ime.api.materiel.dto.ListeMaterielDto;
import java.util.List;

public interface ListeMaterielService {
    ListeMaterielDto createListeMateriel(ListeMaterielDto dto);
    ListeMaterielDto getListeMaterielById(Long id);
    List<ListeMaterielDto> getAllListeMateriels();
    ListeMaterielDto updateListeMateriel(Long id, ListeMaterielDto dto);
    void deleteListeMateriel(Long id);
}