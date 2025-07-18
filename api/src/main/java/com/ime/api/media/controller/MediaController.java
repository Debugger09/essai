package com.ime.api.media.controller;

import com.ime.api.media.model.Media;
import com.ime.api.media.model.EntityType;
import com.ime.api.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    @PostMapping
    public Media save(@RequestBody Media media) {
        return mediaService.save(media);
    }

    @PostMapping("/upload")
    public ResponseEntity<List<Media>> uploadFiles(
            @RequestParam EntityType entityType,
            @RequestParam Long entityId,
            @RequestParam("files") List<MultipartFile> files
    ) throws IOException {
        List<Media> medias = new java.util.ArrayList<>();
        String uploadsPath = System.getProperty("user.dir") + File.separator + "uploads";
        File uploadDir = new File(uploadsPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        for (MultipartFile file : files) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadsPath).resolve(fileName).normalize();
            file.transferTo(filePath.toFile());
            Media media = Media.builder()
                    .nom(file.getOriginalFilename())
                    .type(file.getContentType())
                    .taille(file.getSize())
                    .entityType(entityType)
                    .entityId(entityId)
                    .chemin("/uploads/" + fileName)
                    .build();
            medias.add(mediaService.save(media));
        }
        return new ResponseEntity<>(medias, HttpStatus.CREATED);
    }

    @GetMapping("/entity")
    public List<Media> getMediaForEntity(@RequestParam EntityType entityType, @RequestParam Long entityId) {
        return mediaService.getMediaForEntity(entityType, entityId);
    }

    @GetMapping("/{id}")
    public Media getById(@PathVariable Long id) {
        return mediaService.getById(id);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {
        Media media = mediaService.getById(id);
        if (media == null) {
            System.out.println("[DOWNLOAD] Media id " + id + " non trouvé en base");
            return ResponseEntity.notFound().build();
        }
        // On suppose que chemin est du type "/uploads/filename.ext"
        String cheminRelatif = media.getChemin();
        if (cheminRelatif.startsWith("/")) cheminRelatif = cheminRelatif.substring(1);
        Path filePath = Paths.get("uploads").resolve(Paths.get(cheminRelatif).getFileName().toString()).normalize();
        System.out.println("[DOWNLOAD] Chemin absolu cherché : " + filePath.toAbsolutePath());
        Resource resource = new UrlResource(filePath.toUri());
        System.out.println("[DOWNLOAD] Fichier existe ? " + resource.exists());
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + media.getNom() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, media.getType() != null ? media.getType() : "application/octet-stream")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        mediaService.delete(id);
    }
} 