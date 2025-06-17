package com.ime.api.resetPassword.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordConfirmRequest {

    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;

}
