package com.ime.api.messagerie.ws;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

@Controller
public class PresenceController {
    
    private static final Logger logger = LoggerFactory.getLogger(PresenceController.class);
    private final SimpMessagingTemplate messagingTemplate;
    
    // Stockage des utilisateurs en ligne avec leur session
    private final Map<String, Long> sessionToUserId = new ConcurrentHashMap<>();
    private final Map<Long, String> userIdToSession = new ConcurrentHashMap<>();
    
    public PresenceController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    @MessageMapping("/presence/join")
    public void handleUserJoin(@Payload Map<String, Object> payload, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String sessionId = headerAccessor.getSessionId();
            if (sessionId == null) {
                logger.warn("[Presence] Session ID is null during join");
                return;
            }
            
            Long userId = Long.valueOf(payload.get("userId").toString());
            
            logger.info("[Presence] User {} joining with session {}", userId, sessionId);
            
            // Nettoyer les anciennes sessions de cet utilisateur
            String oldSession = userIdToSession.get(userId);
            if (oldSession != null && !oldSession.equals(sessionId)) {
                sessionToUserId.remove(oldSession);
                logger.info("[Presence] Removed old session {} for user {}", oldSession, userId);
            }
            
            // Enregistrer la nouvelle session
            sessionToUserId.put(sessionId, userId);
            userIdToSession.put(userId, sessionId);
            
            // Notifier tous les utilisateurs de la nouvelle présence
            notifyUserOnline(userId);
            
        } catch (Exception e) {
            logger.error("[Presence] Error handling user join", e);
        }
    }
    
    @MessageMapping("/presence/leave")
    public void handleUserLeave(SimpMessageHeaderAccessor headerAccessor) {
        try {
            String sessionId = headerAccessor.getSessionId();
            if (sessionId == null) {
                logger.warn("[Presence] Session ID is null during leave");
                return;
            }
            
            Long userId = sessionToUserId.remove(sessionId);
            
            if (userId != null) {
                userIdToSession.remove(userId);
                logger.info("[Presence] User {} leaving with session {}", userId, sessionId);
                notifyUserOffline(userId);
            }
            
        } catch (Exception e) {
            logger.error("[Presence] Error handling user leave", e);
        }
    }
    
    @MessageMapping("/presence/request-online-users")
    public void handleRequestOnlineUsers(@Payload Map<String, Object> payload, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String sessionId = headerAccessor.getSessionId();
            if (sessionId == null) {
                logger.warn("[Presence] Session ID is null");
                return;
            }
            
            Long userId = sessionToUserId.get(sessionId);
            
            if (userId != null) {
                Set<Long> onlineUserIds = userIdToSession.keySet();
                Map<String, Object> response = Map.of(
                    "type", "ONLINE_USERS",
                    "userIds", onlineUserIds
                );
                
                // Correction ici : envoi sur une destination unique par session
                messagingTemplate.convertAndSend("/topic/presence." + sessionId, response);
                logger.info("[Presence] Sent online users list to user {}: {}", userId, onlineUserIds);
            }
            
        } catch (Exception e) {
            logger.error("[Presence] Error handling request online users", e);
        }
    }
    
    private void notifyUserOnline(Long userId) {
        Map<String, Object> notification = Map.of(
            "type", "USER_ONLINE",
            "userId", userId
        );
        
        messagingTemplate.convertAndSend("/topic/presence", notification);
        logger.info("[Presence] Notified user online: {}", userId);
    }
    
    private void notifyUserOffline(Long userId) {
        Map<String, Object> notification = Map.of(
            "type", "USER_OFFLINE",
            "userId", userId
        );
        
        messagingTemplate.convertAndSend("/topic/presence", notification);
        logger.info("[Presence] Notified user offline: {}", userId);
    }
    
    // Méthode utilitaire pour vérifier si un utilisateur est en ligne
    public boolean isUserOnline(Long userId) {
        return userIdToSession.containsKey(userId);
    }
    
    // Méthode utilitaire pour obtenir tous les utilisateurs en ligne
    public Set<Long> getOnlineUserIds() {
        return userIdToSession.keySet();
    }
} 