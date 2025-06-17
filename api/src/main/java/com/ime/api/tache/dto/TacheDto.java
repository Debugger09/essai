package com.ime.api.tache.dto;

import com.ime.api.projet.model.Projet;
import com.ime.api.tache.model.enums.PrioriteTache;
import com.ime.api.tache.model.enums.StatutTache;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TacheDto {
    private Long id;
    private String libelle;
    private String description;
    private Date dateEcheance;
    private PrioriteTache priorite;
    private StatutTache statutTache;
    private Projet projet;
    private Date createdAt;
    private Date updatedAt;
}