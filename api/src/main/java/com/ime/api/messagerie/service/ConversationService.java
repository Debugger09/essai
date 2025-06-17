package com.ime.api.messagerie.service;

import java.util.List;

import com.ime.api.messagerie.dto.ConversationDto;

public interface ConversationService {

    ConversationDto createConversation(Long userAId, Long userBId);
    List<ConversationDto> findConversationsByUser(Long userId);
    ConversationDto getConversation(Long id);
    void deleteConversation(Long id);

}
