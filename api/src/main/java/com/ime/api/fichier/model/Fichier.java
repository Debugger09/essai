package com.ime.api.fichier.model;

import com.ime.api.projet.model.Projet;
import com.ime.api.tache.model.Tache;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "fichiers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fichier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String chemin; // chemin ou url du fichier

    private String type; // ex: pdf, image, docx...

    private Long taille; // taille en octets

    @CreationTimestamp
    @Column(updatable = false, name = "date_ajout")
    private Date dateAjout;

    @UpdateTimestamp
    @Column(name = "date_modif")
    private Date dateModif;

    @ManyToOne
    private Projet projet;

    @ManyToOne
    private Tache tache;
}
