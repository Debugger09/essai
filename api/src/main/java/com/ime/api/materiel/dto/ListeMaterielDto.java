package com.ime.api.materiel.dto;

import java.util.Date;


import com.ime.api.tache.dto.TacheDto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListeMaterielDto {
    private Long id;
    private Double quantite;
    private TacheDto tache;
    private MaterielDto materiel;
    private Date createdAt;
    // Ajout des IDs pour la création depuis le frontend
    private Long materielId;
    private Long tacheId;
}