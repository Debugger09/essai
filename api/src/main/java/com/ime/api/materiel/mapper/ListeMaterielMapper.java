package com.ime.api.materiel.mapper;

import com.ime.api.materiel.model.ListeMateriel;
import com.ime.api.materiel.dto.ListeMaterielDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ListeMaterielMapper {
    @Mapping(target = "materielId", ignore = true)
    @Mapping(target = "tacheId", ignore = true)
    ListeMaterielDto toDto(ListeMateriel entity);
    
    ListeMateriel toEntity(ListeMaterielDto dto);
}