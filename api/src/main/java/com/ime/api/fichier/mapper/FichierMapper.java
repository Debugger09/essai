package com.ime.api.fichier.mapper;

import com.ime.api.fichier.model.Fichier;
import com.ime.api.fichier.dto.FichierDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FichierMapper {
    FichierDto toDto(Fichier entity);
    Fichier toEntity(FichierDto dto);
}