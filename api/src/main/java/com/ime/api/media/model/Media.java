package com.ime.api.media.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;

@Entity
@Table(name = "media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String chemin;
    private String type;
    private Long taille;

    @Enumerated(EnumType.STRING)
    private EntityType entityType;
    private Long entityId;

    @CreationTimestamp
    @Column(updatable = false, name = "date_ajout")
    private Date dateAjout;

    @UpdateTimestamp
    @Column(name = "date_modif")
    private Date dateModif;
} 