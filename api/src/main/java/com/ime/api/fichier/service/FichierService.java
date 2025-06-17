package com.ime.api.fichier.service;

import com.ime.api.fichier.dto.FichierDto;
import java.util.List;

public interface FichierService {
    FichierDto createFichier(FichierDto fichierDto);
    FichierDto getFichierById(Long id);
    List<FichierDto> getAllFichiers();
    FichierDto updateFichier(Long id, FichierDto fichierDto);
    void deleteFichier(Long id);
}