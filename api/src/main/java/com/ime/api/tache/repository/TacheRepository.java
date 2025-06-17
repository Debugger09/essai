package com.ime.api.tache.repository;

import com.ime.api.tache.model.Tache;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TacheRepository extends JpaRepository<Tache, Long> {
}