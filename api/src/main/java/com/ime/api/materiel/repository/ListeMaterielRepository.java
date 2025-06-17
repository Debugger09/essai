package com.ime.api.materiel.repository;

import com.ime.api.materiel.model.ListeMateriel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListeMaterielRepository extends JpaRepository<ListeMateriel, Long> {
}