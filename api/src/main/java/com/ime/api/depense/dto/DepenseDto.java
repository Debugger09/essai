package com.ime.api.depense.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepenseDto {
    private Long id;
    private String libelle;
    private double montant;
    // L'id du projet associé à la dépense (pas l'objet Projet complet)
    private Long projetId;
    private Date createdAt;
    private Date updatedAt;
}