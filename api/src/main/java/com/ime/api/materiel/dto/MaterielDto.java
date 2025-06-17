package com.ime.api.materiel.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterielDto {
    private Long id;
    private String libelle;
    private boolean reutilisable;
    private boolean actif;
    private Double quantite;
    private Date createdAt;
    private Date updatedAt;
}