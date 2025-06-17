package com.ime.api.tache.service;

import com.ime.api.tache.dto.ListeMembreDto;
import java.util.List;

public interface ListeMembreService {
    ListeMembreDto createListeMembre(ListeMembreDto dto);
    ListeMembreDto getListeMembreById(Long id);
    List<ListeMembreDto> getAllListeMembres();
    ListeMembreDto updateListeMembre(Long id, ListeMembreDto dto);
    void deleteListeMembre(Long id);
}