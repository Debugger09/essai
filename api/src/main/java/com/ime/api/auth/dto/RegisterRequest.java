package com.ime.api.auth.dto;

import com.ime.api.user.model.enums.ROLE;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private String phone;
    private String job;
    private ROLE role;
}
