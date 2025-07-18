package com.ime.api.messagerie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ime.api.messagerie.model.Conversation;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    // Permet de trouver toutes les conversations d'un utilisateur
    List<Conversation> findByUserA_IdOrUserB_Id(Long userIdA, Long userIdB);

    Conversation findByUserA_IdAndUserB_Id(Long userAId, Long userBId);
}
