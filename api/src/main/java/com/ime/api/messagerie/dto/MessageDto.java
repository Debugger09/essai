package com.ime.api.messagerie.dto;

import com.ime.api.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private String content;
    private Long conversationId;
    private User sender;
    private User receiver;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}