package com.ime.api.tache.mapper;

import com.ime.api.tache.model.Tache;
import com.ime.api.tache.dto.TacheDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TacheMapper {
    @Mapping(source = "projet.id", target = "projetId")
    @Mapping(target = "membres", ignore = true)
    @Mapping(target = "membresIds", ignore = true)
    TacheDto toDto(Tache tache);

    @Mapping(target = "projet", ignore = true)
    Tache toEntity(TacheDto dto);
}