package com.ime.api.user.repository;

import com.ime.api.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Rechercher un utilisateur par email
    Optional<User> findByEmail(String email);

    // Vérifier si un utilisateur existe par email (utile pour inscription)
    boolean existsByEmail(String email);
}
