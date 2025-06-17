package com.ime.api.messagerie.mapper;

import com.ime.api.messagerie.dto.MessageDto;
import com.ime.api.messagerie.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    
    MessageDto toDto(Message message);
    Message toEntity(MessageDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(MessageDto dto, @MappingTarget Message message);
}
