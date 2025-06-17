package com.ime.api.tache.mapper;

import com.ime.api.tache.model.ListeMembre;
import com.ime.api.tache.dto.ListeMembreDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ListeMembreMapper {
    ListeMembreDto toDto(ListeMembre entity);
    ListeMembre toEntity(ListeMembreDto dto);
}