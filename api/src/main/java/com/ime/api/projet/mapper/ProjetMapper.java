package com.ime.api.projet.mapper;

import com.ime.api.projet.model.Projet;
import com.ime.api.projet.dto.ProjetDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjetMapper {
    ProjetDto toDto(Projet projet);
    Projet toEntity(ProjetDto dto);
}
