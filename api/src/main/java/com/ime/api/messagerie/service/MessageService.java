package com.ime.api.messagerie.service;

import com.ime.api.messagerie.dto.MessageDto;
import com.ime.api.messagerie.model.Message;
import java.util.List;

public interface MessageService {
    MessageDto createMessage(MessageDto messageDto);
    List<MessageDto> getMessagesByConversation(Long conversationId);
    MessageDto getMessage(Long id);
    void deleteMessage(Long id);
    boolean updateMessageIfRecent(Long messageId, String newContent);
    Message saveMessage(Message message);
}