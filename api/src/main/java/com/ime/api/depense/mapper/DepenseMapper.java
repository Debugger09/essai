package com.ime.api.depense.mapper;

import com.ime.api.depense.model.Depense;
import com.ime.api.depense.dto.DepenseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DepenseMapper {
    DepenseDto toDto(Depense entity);
    Depense toEntity(DepenseDto dto);
}