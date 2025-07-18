package com.ime.api.messagerie.service.impl;

import com.ime.api.messagerie.dto.ConversationDto;
import com.ime.api.messagerie.mapper.ConversationMapper;
import com.ime.api.messagerie.model.Conversation;
import com.ime.api.messagerie.repository.ConversationRepository;
import com.ime.api.user.model.User;
import com.ime.api.user.repository.UserRepository;
import com.ime.api.messagerie.service.ConversationService;
import com.ime.api.messagerie.ws.WebSocketMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ConversationMapper conversationMapper;
    private final WebSocketMessageService webSocketService;

    @Override
    public ConversationDto createConversation(Long userAId, Long userBId) {
        User userA = userRepository.findById(userAId)
                .orElseThrow(() -> new IllegalArgumentException("UserA not found"));
        User userB = userRepository.findById(userBId)
                .orElseThrow(() -> new IllegalArgumentException("UserB not found"));

        // Vérifier si une conversation existe déjà entre ces deux utilisateurs (dans les deux sens)
        Conversation existing = conversationRepository.findByUserA_IdAndUserB_Id(userAId, userBId);
        if (existing == null) {
            existing = conversationRepository.findByUserA_IdAndUserB_Id(userBId, userAId);
        }
        if (existing != null) {
            return conversationMapper.toDto(existing);
        }

        Conversation conversation = new Conversation();
        conversation.setUserA(userA);
        conversation.setUserB(userB);

        Conversation saved = conversationRepository.save(conversation);
        ConversationDto savedDto = conversationMapper.toDto(saved);

        // Notifier les deux utilisateurs de la nouvelle conversation
        webSocketService.notifyNewConversation(userAId, saved.getId());
        webSocketService.notifyNewConversation(userBId, saved.getId());

        return savedDto;
    }

    @Override
    public List<ConversationDto> findConversationsByUser(Long userId) {
        List<Conversation> conversations = conversationRepository.findByUserA_IdOrUserB_Id(userId, userId);
        return conversations.stream()
                .map(conversationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConversationDto getConversation(Long id) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
        return conversationMapper.toDto(conversation);
    }

    @Override
    public void deleteConversation(Long id) {
        conversationRepository.deleteById(id);
    }
}