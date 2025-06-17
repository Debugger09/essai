package com.ime.api.tache.service;

import com.ime.api.tache.dto.TacheDto;
import java.util.List;

public interface TacheService {
    TacheDto createTache(TacheDto tacheDto);
    TacheDto getTacheById(Long id);
    List<TacheDto> getAllTaches();
    TacheDto updateTache(Long id, TacheDto tacheDto);
    void deleteTache(Long id);
}