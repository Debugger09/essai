package com.ime.api.depense.mapper;

import com.ime.api.depense.model.Depense;
import com.ime.api.depense.dto.DepenseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DepenseMapper {
    @Mapping(source = "projet.id", target = "projetId")
    DepenseDto toDto(Depense entity); 

    @Mapping(target = "projet", ignore = true)
    Depense toEntity(DepenseDto dto);

    java.util.List<DepenseDto> toDtoList(java.util.List<Depense> entities);
    java.util.List<Depense> toEntityList(java.util.List<DepenseDto> dtos);
}