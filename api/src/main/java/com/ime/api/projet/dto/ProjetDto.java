package com.ime.api.projet.dto;

import com.ime.api.projet.enums.StatutProjet;
import com.ime.api.tache.model.Tache;
import com.ime.api.user.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProjetDto {
    private Long id;
    private String name;
    private String description;
    private StatutProjet status;
    private Date dateDebut;
    private Date dateFin;
    private float budget;
    private User chefProjet;
    private List<Tache> taches;
    private Date createdAt;
    private Date updatedAt;
}