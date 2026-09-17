package org.example.togetherhousing.controller;

import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.service.PropertyService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Controller
public class ImageController {

    private final PropertyService propertyService;

    public ImageController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    /**
     * Endpoint to retrieve image stored inside the database (LONGBLOB)
     * Meets the official assignment requirement for database image storage and retrieval.
     */
    @GetMapping("/property/image/{id}")
    public ResponseEntity<byte[]> getPropertyImage(@PathVariable Integer id) {
        Optional<Property> opt = propertyService.getPropertyById(id);
        if (opt.isPresent()) {
            Property p = opt.get();
            // 1. Check database LONGBLOB first
            if (p.getImageData() != null && p.getImageData().length > 0) {
                MediaType mediaType = MediaType.IMAGE_JPEG;
                if (p.getImageContentType() != null) {
                    try {
                        mediaType = MediaType.parseMediaType(p.getImageContentType());
                    } catch (Exception ignored) {
                    }
                }
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(mediaType);
                return new ResponseEntity<>(p.getImageData(), headers, HttpStatus.OK);
            }

            // 2. Check filesystem path stored in property.image
            if (p.getImage() != null && !p.getImage().isEmpty()) {
                String imgPath = p.getImage().startsWith("/") ? p.getImage().substring(1) : p.getImage();
                try {
                    String fsPath = System.getProperty("user.dir") + "/src/main/resources/static/" + imgPath;
                    File f = new File(fsPath);
                    if (f.exists() && f.isFile()) {
                        byte[] bytes = Files.readAllBytes(f.toPath());
                        String probe = Files.probeContentType(f.toPath());
                        MediaType mt = probe != null ? MediaType.parseMediaType(probe) : MediaType.IMAGE_JPEG;
                        return ResponseEntity.ok().contentType(mt).body(bytes);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        // 3. Fallback: serve default static villa image
        try {
            ClassPathResource defaultRes = new ClassPathResource("static/images/villa.jpg");
            if (defaultRes.exists()) {
                byte[] defaultBytes = defaultRes.getInputStream().readAllBytes();
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(defaultBytes);
            }
        } catch (IOException ignored) {
        }

        return ResponseEntity.notFound().build();
    }
}
