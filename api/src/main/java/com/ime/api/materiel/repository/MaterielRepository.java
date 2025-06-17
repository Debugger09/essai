package com.ime.api.materiel.repository;

import com.ime.api.materiel.model.Materiel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterielRepository extends JpaRepository<Materiel, Long> {
}