package com.ime.api.resetPassword.service;

public interface EmailService {
    void sendResetLink(String to, String token);
}
