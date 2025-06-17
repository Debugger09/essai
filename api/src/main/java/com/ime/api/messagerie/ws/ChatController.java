package com.ime.api.messagerie.ws;

import com.ime.api.messagerie.dto.MessageDto;
import com.ime.api.messagerie.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload MessageDto messageDto) {
        MessageDto savedMessage = messageService.createMessage(messageDto);
        messagingTemplate.convertAndSendToUser(
            savedMessage.getReceiver().getId().toString(),
            "/queue/messages",
            savedMessage
        );
    }

    @GetMapping("/api/messages/{conversationId}")
    @ResponseBody
    public List<MessageDto> getMessages(@PathVariable Long conversationId) {
        return messageService.getMessagesByConversation(conversationId);
    }

    @GetMapping("/api/messages/user/{userId}")
    @ResponseBody
    public List<MessageDto> getUserMessages(@PathVariable Long userId) {
        return messageService.getMessagesByConversation(userId);
    }

    @DeleteMapping("/api/messages/{messageId}")
    @ResponseBody
    public void deleteMessage(@PathVariable Long messageId) {
        messageService.deleteMessage(messageId);
    }

    @PutMapping("/api/messages/{messageId}")
    @ResponseBody
    public boolean updateMessage(
            @PathVariable Long messageId,
            @RequestParam String newContent) {
        return messageService.updateMessageIfRecent(messageId, newContent);
    }
} 