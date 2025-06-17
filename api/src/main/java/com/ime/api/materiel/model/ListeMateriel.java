package com.ime.api.materiel.model;

import com.ime.api.tache.model.Tache;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "liste_materiel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListeMateriel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double quantite;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @ManyToOne
    private Tache tache;

    @ManyToOne
    private Materiel materiel;
}
