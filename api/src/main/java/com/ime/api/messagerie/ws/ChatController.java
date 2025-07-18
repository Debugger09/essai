package com.ime.api.messagerie.ws;

import com.ime.api.messagerie.dto.MessageDto;
import com.ime.api.messagerie.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final MessageService messageService;
    private final WebSocketMessageService webSocketMessageService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload MessageDto messageDto) {
        MessageDto savedMessage = messageService.createMessage(messageDto);
        
        // Notifier les utilisateurs de la conversation
        if (savedMessage.getConversationId() != null) {
            webSocketMessageService.notifyConversationUpdate(savedMessage.getConversationId(), savedMessage);
        }
        
        // Notifier l'expéditeur et le destinataire
        if (savedMessage.getSender() != null && savedMessage.getSender().getId() != null) {
            webSocketMessageService.notifyNewMessage(savedMessage.getSender().getId(), savedMessage);
        }
        if (savedMessage.getReceiver() != null && savedMessage.getReceiver().getId() != null) {
            webSocketMessageService.notifyNewMessage(savedMessage.getReceiver().getId(), savedMessage);
        }
    }
} 