package com.ime.api.messagerie.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.ime.api.user.model.User; // assuming User is in another package

@Entity
@Table(name = "conversations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conversation")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user_a", nullable = false)
    private User userA;

    @ManyToOne
    @JoinColumn(name = "id_user_b", nullable = false)
    private User userB;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
