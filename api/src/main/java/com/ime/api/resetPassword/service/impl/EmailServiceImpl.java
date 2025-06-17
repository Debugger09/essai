package com.ime.api.resetPassword.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ime.api.resetPassword.service.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendResetLink(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Réinitialisation de mot de passe");

            // Mets ici l'URL de ton front Nuxt
            String resetURL = "http://localhost:3000/reset-password?token=" + token;
            helper.setText("Veuillez cliquer sur le lien suivant afin de réinitialiser votre mot de passe : " + resetURL, false);

            mailSender.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Échec de l'envoi d'email.");
        }
    }
}
