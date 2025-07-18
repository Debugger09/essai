package com.ime.api.messagerie.mapper;

import com.ime.api.messagerie.dto.MessageDto;
import com.ime.api.messagerie.model.Message;
import com.ime.api.messagerie.model.Conversation;
import com.ime.api.messagerie.repository.ConversationRepository;
import org.mapstruct.Context;
import org.mapstruct.Named;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    
    @Mapping(source = "conversation.id", target = "conversationId")
    MessageDto toDto(Message message);

    // Uniquement ce mapping pour 'conversation' (pas de doublon)
    @Mapping(source = "conversationId", target = "conversation", qualifiedByName = "conversationFromId")
    Message toEntity(MessageDto dto, @Context ConversationRepository conversationRepository);

    @Named("conversationFromId")
    default Conversation conversationFromId(Long id, @Context ConversationRepository conversationRepository) {
        if (id == null) return null;
        return conversationRepository.findById(id).orElse(null);
    }

    // Ne pas mapper 'conversation' ici pour éviter l'avertissement
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "conversation", ignore = true)
    void updateEntityFromDto(MessageDto dto, @MappingTarget Message message, @Context ConversationRepository conversationRepository);
}
