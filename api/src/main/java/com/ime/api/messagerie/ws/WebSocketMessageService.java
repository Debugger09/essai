package com.ime.api.messagerie.ws;

import com.ime.api.messagerie.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketMessageService {
    
    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessageToConversation(Long conversationId, MessageDto message) {
        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, message);
    }

    public void notifyNewConversation(Long userId, Long conversationId) {
        messagingTemplate.convertAndSend("/topic/user/" + userId + "/conversations", conversationId);
    }
} 