package com.ime.api.depense.repository;

import com.ime.api.depense.model.Depense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepenseRepository extends JpaRepository<Depense, Long> {
}