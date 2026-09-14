package org.example.togetherhousing.service;

import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.model.PropertyStatus;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public Property addProperty(Property property, UserTbl seller) {
        property.setSeller(seller);
        property.setStatus(PropertyStatus.PENDING);
        property.setCreatedAt(LocalDateTime.now());

        if (property.getPrice() != null && (property.getPriceFormatted() == null || property.getPriceFormatted().isEmpty())) {
            property.setPriceFormatted(formatPrice(property.getPrice()));
        }

        if (property.getImage() == null || property.getImage().trim().isEmpty()) {
            if ("apartment".equalsIgnoreCase(property.getPropertyType())) {
                property.setImage("images/apartment.jpg");
            } else if ("land".equalsIgnoreCase(property.getPropertyType())) {
                property.setImage("images/mountain_land.jpg");
            } else {
                property.setImage("images/villa.jpg");
            }
        }

        return propertyRepository.save(property);
    }

    public List<Property> getPropertiesBySeller(UserTbl seller) {
        return propertyRepository.findBySeller(seller);
    }

    public List<Property> getApprovedProperties() {
        return propertyRepository.findByStatus(PropertyStatus.APPROVED);
    }

    public List<Property> getPendingProperties() {
        return propertyRepository.findByStatus(PropertyStatus.PENDING);
    }

    public Optional<Property> getPropertyById(Integer id) {
        return propertyRepository.findById(id);
    }

    public Property approveProperty(Integer id) {
        Property property = propertyRepository.findById(id).orElse(null);
        if (property != null) {
            property.setStatus(PropertyStatus.APPROVED);
            property.setRejectionReason(null);
            return propertyRepository.save(property);
        }
        return null;
    }

    public Property rejectProperty(Integer id, String reason) {
        Property property = propertyRepository.findById(id).orElse(null);
        if (property != null) {
            property.setStatus(PropertyStatus.REJECTED);
            property.setRejectionReason(reason);
            return propertyRepository.save(property);
        }
        return null;
    }

    public void deleteProperty(Integer id) {
        propertyRepository.deleteById(id);
    }

    private String formatPrice(Double price) {
        if (price == null) return "Rs 0";
        long val = price.longValue();
        if (val >= 10000000) {
            double crore = (double) val / 10000000.0;
            return String.format("Rs %.2f Cr", crore);
        } else if (val >= 100000) {
            double lakh = (double) val / 100000.0;
            return String.format("Rs %.2f Lakh", lakh);
        }
        return "Rs " + val;
    }
}
