package com.ime.api.projet.mapper;

import com.ime.api.projet.model.Projet;
import com.ime.api.projet.dto.ProjetDto;
import com.ime.api.depense.mapper.DepenseMapper;
import com.ime.api.tache.mapper.TacheMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DepenseMapper.class, TacheMapper.class})
public interface ProjetMapper {
    @Mapping(source = "depenses", target = "depenses")
    ProjetDto toDto(Projet projet);
    @Mapping(source = "depenses", target = "depenses")
    Projet toEntity(ProjetDto dto);
}
