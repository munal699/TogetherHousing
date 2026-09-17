package org.example.togetherhousing.controller.api;

import org.example.togetherhousing.dto.ApiResponse;
import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.service.PropertyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "*")
public class PropertyRestController {

    private final PropertyService propertyService;

    public PropertyRestController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Property>>> getApprovedProperties() {
        List<Property> properties = propertyService.getApprovedProperties();
        return ResponseEntity.ok(ApiResponse.ok("Approved properties retrieved successfully", properties));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Property>> getPropertyById(@PathVariable("id") Integer id) {
        return propertyService.getPropertyById(id)
                .map(p -> ResponseEntity.ok(ApiResponse.ok("Property found", p)))
                .orElseGet(() -> ResponseEntity.status(404).body(ApiResponse.error("Property not found with id: " + id)));
    }
}
