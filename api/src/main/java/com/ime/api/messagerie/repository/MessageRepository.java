package com.ime.api.messagerie.repository;

import com.ime.api.messagerie.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationId(Long conversationId);
    List<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
