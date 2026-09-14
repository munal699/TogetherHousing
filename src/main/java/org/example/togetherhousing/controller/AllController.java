package org.example.togetherhousing.controller;

import jakarta.servlet.http.HttpSession;
import org.example.togetherhousing.model.Notification;
import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.userRepository;
import org.example.togetherhousing.service.NotificationService;
import org.example.togetherhousing.service.PropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AllController {

    private final userRepository uRepo;
    private final PropertyService propertyService;
    private final NotificationService notificationService;

    public AllController(userRepository uRepo, PropertyService propertyService, NotificationService notificationService) {
        this.uRepo = uRepo;
        this.propertyService = propertyService;
        this.notificationService = notificationService;
    }

    @GetMapping("/")
    public String firstPage() {
        return "firstPage";
    }

    @GetMapping("/buyer-dashboard")
    public String buyerDashboard() {
        return "buyer-dashboard";
    }

    @GetMapping("/seller-dashboard")
    public String sellerDashboard(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email != null) {
            UserTbl seller = uRepo.findByEmail(email).orElse(null);
            if (seller != null) {
                List<Property> myProperties = propertyService.getPropertiesBySeller(seller);
                model.addAttribute("myProperties", myProperties);

                List<Notification> notifications = notificationService.getUserNotifications(seller);
                model.addAttribute("notifications", notifications);

                long unreadCount = notificationService.getUnreadCount(seller);
                model.addAttribute("unreadCount", unreadCount);
            }
        }
        return "seller-dashboard";
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model) {
        List<Property> pendingProperties = propertyService.getPendingProperties();
        List<UserTbl> allUsers = uRepo.findAll();

        model.addAttribute("pendingProperties", pendingProperties);
        model.addAttribute("allUsers", allUsers);

        return "admin-dashboard";
    }

    @GetMapping("/home")
    public String homeGet(Model m) {
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/services")
    public String services() {
        return "services";
    }
}
