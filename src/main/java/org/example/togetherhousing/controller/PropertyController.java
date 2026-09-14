package org.example.togetherhousing.controller;

import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.userRepository;
import org.example.togetherhousing.service.PropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class PropertyController {

    private final userRepository userRepository;
    private final PropertyService propertyService;

    public PropertyController(userRepository userRepository, PropertyService propertyService) {
        this.userRepository = userRepository;
        this.propertyService = propertyService;
    }

    @GetMapping("/properties")
    public String propertiesPage(Model model) {
        List<Property> approvedProperties = propertyService.getApprovedProperties();
        model.addAttribute("approvedProperties", approvedProperties);
        return "properties";
    }

    @GetMapping("/property-detail")
    public String propertyDetail(
            @RequestParam Integer id,
            @RequestParam(required = false) String seller,
            Model model
    ) {
        Optional<Property> optProperty = propertyService.getPropertyById(id);
        if (optProperty.isPresent()) {
            Property property = optProperty.get();
            model.addAttribute("property", property);
            model.addAttribute("seller", property.getSeller());
            return "property-detail";
        }

        // Fallback: try old behavior with seller name
        if (seller != null) {
            UserTbl sellerUser = userRepository.findByFullname(seller).orElse(null);
            if (sellerUser != null) {
                model.addAttribute("seller", sellerUser);
                model.addAttribute("propertyId", id);
                return "property-detail";
            }
        }

        return "redirect:/properties";
    }
}
