package com.ime.api.media.service.impl;

import com.ime.api.media.model.Media;
import com.ime.api.media.model.EntityType;
import com.ime.api.media.repository.MediaRepository;
import com.ime.api.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository;

    static {
        String uploadsPath = System.getProperty("user.dir") + java.io.File.separator + "uploads";
        Path uploadDir = Paths.get(uploadsPath);
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
                System.out.println("[UPLOADS] Dossier créé à : " + uploadDir.toAbsolutePath());
            } else {
                System.out.println("[UPLOADS] Dossier déjà existant à : " + uploadDir.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("[UPLOADS] Erreur lors de la création du dossier uploads : " + e.getMessage());
            throw new RuntimeException("Impossible de créer le dossier uploads", e);
        }
    }

    @Override
    public Media save(Media media) {
        return mediaRepository.save(media);
    }

    @Override
    public List<Media> getMediaForEntity(EntityType entityType, Long entityId) {
        return mediaRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Override
    public void delete(Long id) {
        mediaRepository.deleteById(id);
    }

    @Override
    public Media getById(Long id) {
        return mediaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Media> uploadFiles(EntityType entityType, Long entityId, List<MultipartFile> files) throws IOException {
        List<Media> medias = new java.util.ArrayList<>();
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        for (MultipartFile file : files) {
            // Sauvegarde physique du fichier
            String originalFilename = file.getOriginalFilename();
            Path filePath = uploadDir.resolve(originalFilename);
            Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            // Enregistrement en base
            Media media = Media.builder()
                    .nom(originalFilename)
                    .type(file.getContentType())
                    .taille(file.getSize())
                    .entityType(entityType)
                    .entityId(entityId)
                    .chemin("/uploads/" + originalFilename)
                    .build();
            medias.add(mediaRepository.save(media));
        }
        return medias;
    }
} 