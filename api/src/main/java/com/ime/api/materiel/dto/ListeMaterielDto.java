package com.ime.api.materiel.dto;

import java.util.Date;

import com.ime.api.materiel.model.Materiel;
import com.ime.api.tache.model.Tache;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListeMaterielDto {
    private Long id;
    private Double quantite;
    private Tache tache;
    private Materiel materiel;
    private Date createdAt;
    // Ajout des IDs pour la création depuis le frontend
    private Long materielId;
    private Long tacheId;
}