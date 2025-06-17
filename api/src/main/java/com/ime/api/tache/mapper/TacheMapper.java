package com.ime.api.tache.mapper;

import com.ime.api.tache.model.Tache;
import com.ime.api.tache.dto.TacheDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TacheMapper {
    TacheDto toDto(Tache tache);
    Tache toEntity(TacheDto dto);
}