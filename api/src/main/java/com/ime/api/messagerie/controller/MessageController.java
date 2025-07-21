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
        try {
            MessageDto savedMessage = messageService.createMessage(dto);
            log.info("[DEBUG] Appel notifyConversationUpdate pour conversationId={}", savedMessage.getConversationId());
            webSocketMessageService.notifyConversationUpdate(savedMessage.getConversationId(), savedMessage);
            return ResponseEntity.ok(savedMessage);
        } catch (Exception e) {
            log.error("[ERROR] Exception lors de la création du message", e);
            return ResponseEntity.status(500).build();
        }
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