package com.ime.api.fichier.dto;

import java.util.Date;

import com.ime.api.projet.model.Projet;
import com.ime.api.tache.model.Tache;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FichierDto {
    private Long id;
    private String nom;
    private String chemin;
    private String type;
    private Long taille;
    private Projet projet;
    private Tache tache;
    private Date dateAjout;
    private Date dateModif;
}