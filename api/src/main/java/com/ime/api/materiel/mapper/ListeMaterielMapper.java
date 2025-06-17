package com.ime.api.materiel.mapper;

import com.ime.api.materiel.model.ListeMateriel;
import com.ime.api.materiel.dto.ListeMaterielDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ListeMaterielMapper {
    ListeMaterielDto toDto(ListeMateriel entity);
    ListeMateriel toEntity(ListeMaterielDto dto);
}