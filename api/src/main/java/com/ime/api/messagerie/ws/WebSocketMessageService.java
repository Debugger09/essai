package com.ime.api.messagerie.ws;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WebSocketMessageService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketMessageService.class);
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketMessageService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyNewConversation(Long userId, Long conversationId) {
        messagingTemplate.convertAndSend("/topic/user/" + userId + "/conversations", conversationId);
    }

    public void notifyNewMessage(Long userId, Object message) {
        messagingTemplate.convertAndSend("/topic/user/" + userId + "/messages", message);
    }

    public void notifyConversationUpdate(Long conversationId, Object update) {
        System.out.println("[WebSocket][WebSocketMessageService] Envoi du message sur le topic /topic/conversation/" + conversationId + " : " + update);
        logger.info("[WebSocket] Envoi du message sur le topic /topic/conversation/{} : {}", conversationId, update);
        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, update);
    }
} 