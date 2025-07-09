package com.ime.api.fichier.repository;

import com.ime.api.fichier.model.Fichier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FichierRepository extends JpaRepository<Fichier, Long> {
    List<Fichier> findByProjetId(Long projetId);
}