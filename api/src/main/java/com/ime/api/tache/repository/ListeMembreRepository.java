package com.ime.api.tache.repository;

import com.ime.api.tache.model.ListeMembre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListeMembreRepository extends JpaRepository<ListeMembre, Long> {
}