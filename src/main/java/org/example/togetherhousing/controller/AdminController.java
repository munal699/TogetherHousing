package org.example.togetherhousing.controller;

import jakarta.servlet.http.HttpSession;
import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.service.NotificationService;
import org.example.togetherhousing.service.PropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    private final PropertyService propertyService;
    private final NotificationService notificationService;

    public AdminController(PropertyService propertyService, NotificationService notificationService) {
        this.propertyService = propertyService;
        this.notificationService = notificationService;
    }

    @PostMapping("/admin/property/approve")
    public String approveProperty(@RequestParam Integer propertyId, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role == null || !"ADMIN".equalsIgnoreCase(role.trim())) {
            return "redirect:/login?error=unauthorized";
        }

        Property property = propertyService.approveProperty(propertyId);
        if (property != null && property.getSeller() != null) {
            notificationService.sendNotification(
                property.getSeller(),
                property,
                "Your property '" + property.getTitle() + "' has been APPROVED by Administrator and is now live on the public catalog!",
                "APPROVAL"
            );
        }
        return "redirect:/admin-dashboard?success=property_approved";
    }

    @PostMapping("/admin/property/reject")
    public String rejectProperty(
            @RequestParam Integer propertyId,
            @RequestParam(required = false) String reason,
            HttpSession session
    ) {
        String role = (String) session.getAttribute("role");
        if (role == null || !"ADMIN".equalsIgnoreCase(role.trim())) {
            return "redirect:/login?error=unauthorized";
        }

        String finalReason = (reason != null && !reason.trim().isEmpty())
                ? reason.trim()
                : "Rejected by Administrator";

        Property property = propertyService.rejectProperty(propertyId, finalReason);
        if (property != null && property.getSeller() != null) {
            notificationService.sendNotification(
                property.getSeller(),
                property,
                "Your property '" + property.getTitle() + "' was REJECTED by Administrator. Reason: " + finalReason,
                "REJECTION"
            );
        }
        return "redirect:/admin-dashboard?success=property_rejected";
    }
}
