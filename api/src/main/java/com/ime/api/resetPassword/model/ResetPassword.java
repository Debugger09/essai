package com.ime.api.resetPassword.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;

import com.ime.api.user.model.User;

@Entity
@Table(name = "reset_pass_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Column(unique = true, nullable = false)
    private String token = UUID.randomUUID().toString();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date expiration;

    @PrePersist
    public void prePersist() {
        if (expiration == null) {
            // Token expire in 1 hour by default
            expiration = new Date(System.currentTimeMillis() + 3600000);
        }
    }
}
