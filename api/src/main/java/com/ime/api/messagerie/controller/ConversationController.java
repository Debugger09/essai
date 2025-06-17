package com.ime.api.messagerie.controller;

import com.ime.api.messagerie.dto.ConversationDto;
import com.ime.api.messagerie.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping
    public ResponseEntity<ConversationDto> create(@RequestParam Long userAId, @RequestParam Long userBId) {
        return ResponseEntity.ok(conversationService.createConversation(userAId, userBId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ConversationDto>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(conversationService.findConversationsByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(conversationService.getConversation(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        conversationService.deleteConversation(id);
        return ResponseEntity.noContent().build();
    }
}