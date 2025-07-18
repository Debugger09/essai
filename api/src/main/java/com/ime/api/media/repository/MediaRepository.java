package com.ime.api.media.repository;

import com.ime.api.media.model.Media;
import com.ime.api.media.model.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByEntityTypeAndEntityId(EntityType entityType, Long entityId);
} 