package com.ime.api.tache.repository;

import com.ime.api.tache.model.ListeMembre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListeMembreRepository extends JpaRepository<ListeMembre, Long> {
    List<ListeMembre> findByTacheId(Long tacheId);
}