package com.ime.api.resetPassword.repository;

import com.ime.api.user.model.User;
import com.ime.api.resetPassword.model.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {
    Optional<ResetPassword> findByUser(User user);

    @Transactional
    @Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM ResetPassword r WHERE r.user.id = :userId")
    void deleteByUserId(Long userId);

    Optional<ResetPassword> findByToken(String token);
}
