package com.ime.api.tache.dto;

import java.util.Date;

import com.ime.api.tache.model.Tache;
import com.ime.api.user.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListeMembreDto {
    private Long id;
    private User membre;
    private Tache tache;
    private Date createdAt;
    private Date updatedAt;
}