package com.ime.api.messagerie.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.ime.api.messagerie.model.Conversation;
import com.ime.api.messagerie.dto.ConversationDto;

@Mapper(componentModel = "spring", uses = {MessageMapper.class})
public interface ConversationMapper {

    @Mappings({
        @Mapping(source = "userA.id", target = "userAId"),
        @Mapping(source = "userB.id", target = "userBId"),
        @Mapping(source = "messages", target = "messages")
    })
    ConversationDto toDto(Conversation conversation);

    // Pour la conversion inverse, il faudra gérer la récupération des User à partir des IDs dans le service
    // Ici, on laisse les champs userA et userB à null (à compléter dans le service si besoin)
    @Mappings({
        @Mapping(target = "userA", ignore = true),
        @Mapping(target = "userB", ignore = true),
        @Mapping(source = "messages", target = "messages")
    })
    Conversation toEntity(ConversationDto conversationDto);
}
