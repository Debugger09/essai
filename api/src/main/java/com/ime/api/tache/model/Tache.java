package com.ime.api.tache.model;

import com.ime.api.tache.model.enums.PrioriteTache;
import com.ime.api.tache.model.enums.StatutTache;
import com.ime.api.projet.model.Projet;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "taches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;
    private String description;
    private Date dateEcheance;

    @Enumerated(EnumType.STRING)
    private PrioriteTache priorite;

    @Enumerated(EnumType.STRING)
    private StatutTache statutTache = StatutTache.A_FAIRE;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    private Projet projet;
}
