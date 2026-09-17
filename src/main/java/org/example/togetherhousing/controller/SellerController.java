package org.example.togetherhousing.controller;

import jakarta.servlet.http.HttpSession;
import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.userRepository;
import org.example.togetherhousing.service.PropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class SellerController {

    private final PropertyService propertyService;
    private final userRepository userRepository;

    public SellerController(PropertyService propertyService, userRepository userRepository) {
        this.propertyService = propertyService;
        this.userRepository = userRepository;
    }

    @PostMapping("/seller/property/add")
    public String addProperty(
            @RequestParam String title,
            @RequestParam String propertyType,
            @RequestParam Double price,
            @RequestParam String location,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false) Integer bathrooms,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile imageFile,
            HttpSession session
    ) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }

        UserTbl seller = userRepository.findByEmail(email).orElse(null);
        if (seller == null) {
            return "redirect:/login";
        }

        Property property = new Property();
        property.setTitle(title);
        property.setPropertyType(propertyType);
        property.setPrice(price);
        property.setLocation(location);
        property.setArea(area);
        property.setBedrooms(bedrooms);
        property.setBathrooms(bathrooms);
        property.setDescription(description);

        // Handle image upload (stores in DB LONGBLOB + filesystem backup)
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Store directly in database as required by assignment
                property.setImageData(imageFile.getBytes());
                property.setImageContentType(imageFile.getContentType() != null ? imageFile.getContentType() : "image/jpeg");

                // Also maintain filesystem copy as secondary backup
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/uploads/";
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String originalFilename = imageFile.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String newFilename = UUID.randomUUID().toString() + extension;
                Path filePath = Paths.get(uploadDir + newFilename);
                Files.write(filePath, imageFile.getBytes());
                property.setImage("images/uploads/" + newFilename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        propertyService.addProperty(property, seller);

        return "redirect:/seller-dashboard?success=property_added";
    }
}
