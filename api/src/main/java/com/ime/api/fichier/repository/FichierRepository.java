package com.ime.api.fichier.repository;

import com.ime.api.fichier.model.Fichier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FichierRepository extends JpaRepository<Fichier, Long> {
}