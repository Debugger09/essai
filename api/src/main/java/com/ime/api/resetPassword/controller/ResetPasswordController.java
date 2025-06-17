package com.ime.api.resetPassword.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.ime.api.resetPassword.service.ResetPasswordService;
import com.ime.api.resetPassword.dto.ResetPasswordRequest;


import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/resetPassword")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService; // Correction ici

    @PostMapping("/forgot")
    public ResponseEntity<String> sendResetLink(@RequestBody ResetPasswordRequest request) {
        resetPasswordService.sendResetLink(request.getEmail());
        return ResponseEntity.ok("Un lien de réinitialisation a été envoyé.");
    }
    
    @PostMapping("/reset")
    public ResponseEntity<String> resetPass(@RequestParam String token, @RequestParam String newPass) {
        boolean success = resetPasswordService.resetPassword(token, newPass);
        if (success) {
            return ResponseEntity.ok("Le mot de passe a été mis à jour.");
        } else {
            return ResponseEntity.badRequest().body("Le lien de réinitialisation n'est plus valide.");
        }
    }
}
