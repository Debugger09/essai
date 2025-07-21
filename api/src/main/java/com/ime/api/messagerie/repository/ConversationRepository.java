package com.ime.api.messagerie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ime.api.messagerie.model.Conversation;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    // Permet de trouver toutes les conversations d'un utilisateur
    List<Conversation> findByUserA_IdOrUserB_Id(Long userIdA, Long userIdB);

    Conversation findByUserA_IdAndUserB_Id(Long userAId, Long userBId);
    
    // Requête pour récupérer les conversations avec le dernier message
    @Query("SELECT DISTINCT c FROM Conversation c " +
           "LEFT JOIN FETCH c.messages m " +
           "WHERE c.userA.id = :userId OR c.userB.id = :userId " +
           "ORDER BY c.updatedAt DESC")
    List<Conversation> findConversationsWithMessagesByUser(@Param("userId") Long userId);
}
