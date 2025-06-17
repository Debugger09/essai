package com.ime.api.resetPassword.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ime.api.resetPassword.model.ResetPassword;
import com.ime.api.resetPassword.repository.ResetPasswordRepository;
import com.ime.api.resetPassword.service.EmailService;
import com.ime.api.resetPassword.service.ResetPasswordService;
import com.ime.api.user.model.User;
import com.ime.api.user.repository.UserRepository;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final UserRepository userRepository;
    private final ResetPasswordRepository resetPassRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService; // A ajouter plus tard si tu veux envoyer le mail

    @Override
    @Transactional
    public void sendResetLink(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Supprime tous les anciens tokens pour cet utilisateur
        resetPassRepository.deleteByUserId(user.getId());

        ResetPassword token = ResetPassword.builder()
                .token(UUID.randomUUID().toString())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .user(user)
                .build();

        resetPassRepository.save(token);

        emailService.sendResetLink(email, token.getToken());
    }

    @Override
    public boolean resetPassword(String tokenStr, String newPass) {
        ResetPassword token = resetPassRepository.findByToken(tokenStr)
                .orElseThrow(() -> new RuntimeException("Token introuvable"));

        if (token.getExpiration().before(new Date())) {
            return false; // Token expiré
        }
        
        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPass)); // Encodage du mot de passe
        userRepository.save(user);
        resetPassRepository.delete(token);
        return true;
    }
}
