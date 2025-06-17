package com.ime.api.materiel.mapper;

import com.ime.api.materiel.model.Materiel;
import com.ime.api.materiel.dto.MaterielDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MaterielMapper {
    MaterielDto toDto(Materiel entity);
    Materiel toEntity(MaterielDto dto);
}