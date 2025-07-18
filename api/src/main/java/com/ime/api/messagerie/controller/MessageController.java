package com.ime.api.messagerie.controller;

import com.ime.api.messagerie.dto.MessageDto;
import com.ime.api.messagerie.service.MessageService;
import com.ime.api.messagerie.ws.WebSocketMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final WebSocketMessageService webSocketMessageService;

    @PostMapping
    public ResponseEntity<MessageDto> create(@RequestBody MessageDto dto) {
        log.debug("Creating message: {}", dto);
        MessageDto savedMessage = messageService.createMessage(dto);
        
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
        
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageDto>> getByConversation(@PathVariable Long conversationId) {
        return ResponseEntity.ok(messageService.getMessagesByConversation(conversationId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Long id, @RequestBody String contenu) {
        return ResponseEntity.ok(messageService.updateMessageIfRecent(id, contenu));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}