package com.ime.api.user.dto;

import com.ime.api.user.model.enums.ROLE;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private Long id;
    private String lastName;
    private String firstName;
    private String email;
    private String password;  // <-- ajouté
    private String phone;
    private String job;
    private boolean status;
    private ROLE role;
    private Date createdAt;
    private Date updatedAt;
}
