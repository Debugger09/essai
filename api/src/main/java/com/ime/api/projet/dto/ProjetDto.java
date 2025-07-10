package com.ime.api.projet.dto;

import com.ime.api.projet.enums.StatutProjet;
import com.ime.api.tache.dto.TacheDto;
import com.ime.api.user.dto.UserDTO;
import com.ime.api.depense.dto.DepenseDto;
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
    private UserDTO chefProjet;
    private List<TacheDto> taches;
    private List<DepenseDto> depenses;
    private Date createdAt;
    private Date updatedAt;
}