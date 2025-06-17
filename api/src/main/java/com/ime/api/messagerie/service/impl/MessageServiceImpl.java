package com.ime.api.messagerie.service.impl;

import com.ime.api.messagerie.dto.MessageDto;
import com.ime.api.messagerie.model.Message;
import com.ime.api.messagerie.repository.MessageRepository;
import com.ime.api.messagerie.service.MessageService;
import com.ime.api.user.model.User;
import com.ime.api.user.repository.UserRepository;
import com.ime.api.messagerie.model.Conversation;
import com.ime.api.messagerie.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;

    @Override
    @Transactional
    public MessageDto createMessage(MessageDto messageDto) {
        Message message = new Message();
        message.setContent(messageDto.getContent());
        
        Conversation conversation = conversationRepository.findById(messageDto.getConversation().getId())
            .orElseThrow(() -> new RuntimeException("Conversation not found"));
        message.setConversation(conversation);
        
        User sender = userRepository.findById(messageDto.getSender().getId())
            .orElseThrow(() -> new RuntimeException("Sender not found"));
        message.setSender(sender);
        
        User receiver = userRepository.findById(messageDto.getReceiver().getId())
            .orElseThrow(() -> new RuntimeException("Receiver not found"));
        message.setReceiver(receiver);
        
        Message savedMessage = messageRepository.save(message);
        return convertToDto(savedMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDto> getMessagesByConversation(Long conversationId) {
        return messageRepository.findByConversationId(conversationId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDto getMessage(Long id) {
        return messageRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean updateMessageIfRecent(Long messageId, String newContent) {
        return messageRepository.findById(messageId)
                .map(message -> {
                    if (message.getCreatedAt().plusMinutes(5).isAfter(LocalDateTime.now())) {
                        message.setContent(newContent);
                        messageRepository.save(message);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    private MessageDto convertToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setConversation(message.getConversation());
        dto.setSender(message.getSender());
        dto.setReceiver(message.getReceiver());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setUpdatedAt(message.getUpdatedAt());
        return dto;
    }

    @Override
    @Transactional
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }
}