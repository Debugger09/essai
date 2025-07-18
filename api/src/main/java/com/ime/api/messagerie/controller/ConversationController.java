package com.ime.api.messagerie.controller;

import com.ime.api.messagerie.dto.ConversationDto;
import com.ime.api.messagerie.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
@Slf4j
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping
    public ResponseEntity<ConversationDto> create(@RequestBody ConversationDto conversationDto) {
        log.debug("Creating conversation with userAId: {} and userBId: {}", conversationDto.getUserAId(), conversationDto.getUserBId());
        if (conversationDto.getUserAId() == null || conversationDto.getUserBId() == null) {
            log.warn("Missing required parameters: userAId={}, userBId={}", conversationDto.getUserAId(), conversationDto.getUserBId());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(conversationService.createConversation(conversationDto.getUserAId(), conversationDto.getUserBId()));
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