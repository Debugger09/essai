package com.ime.api.media.service;

import com.ime.api.media.model.Media;
import com.ime.api.media.model.EntityType;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface MediaService {
    Media save(Media media);
    List<Media> getMediaForEntity(EntityType entityType, Long entityId);
    void delete(Long id);
    Media getById(Long id);
    List<Media> uploadFiles(EntityType entityType, Long entityId, List<MultipartFile> files) throws IOException;
} 