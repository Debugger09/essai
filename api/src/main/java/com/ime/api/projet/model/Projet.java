package com.ime.api.projet.model;

import com.ime.api.projet.enums.StatutProjet;
import com.ime.api.user.model.User;
import com.ime.api.tache.model.Tache;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "projets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 200)
    private String description;

    @Enumerated(EnumType.STRING)
    private StatutProjet status = StatutProjet.EN_COURS;

    private Date dateDebut;
    private Date dateFin;
    private float budget;

    @ManyToOne
    private User chefProjet;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tache> taches = new ArrayList<>();
}
