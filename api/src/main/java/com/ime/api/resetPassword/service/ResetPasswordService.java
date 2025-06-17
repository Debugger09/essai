package com.ime.api.resetPassword.service;


public interface ResetPasswordService {

    void sendResetLink(String email);
    boolean resetPassword(String token, String newPass);
}
