package org.example.togetherhousing.controller;

import jakarta.servlet.http.HttpSession;
import org.example.togetherhousing.model.Booking;
import org.example.togetherhousing.model.BookingStatus;
import org.example.togetherhousing.model.Notification;
import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.userRepository;
import org.example.togetherhousing.service.BookingService;
import org.example.togetherhousing.service.ChatService;
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
    private final ChatService chatService;
    private final BookingService bookingService;

    public AllController(userRepository uRepo,
                         PropertyService propertyService,
                         NotificationService notificationService,
                         ChatService chatService,
                         BookingService bookingService) {
        this.uRepo = uRepo;
        this.propertyService = propertyService;
        this.notificationService = notificationService;
        this.chatService = chatService;
        this.bookingService = bookingService;
    }

    @GetMapping("/")
    public String firstPage() {
        return "firstPage";
    }

    @GetMapping("/buyer-dashboard")
    public String buyerDashboard(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email != null) {
            UserTbl buyer = uRepo.findByEmail(email).orElse(null);
            if (buyer != null) {
                model.addAttribute("buyer", buyer);
                List<Booking> myBookings = bookingService.getBuyerBookings(buyer);
                model.addAttribute("myBookings", myBookings);
                model.addAttribute("bookingCount", myBookings.size());

                double totalPropertyValue = myBookings.stream()
                        .mapToDouble(b -> b.getTotalPrice() != null ? b.getTotalPrice() : 0.0)
                        .sum();
                double totalDownPaid = myBookings.stream()
                        .mapToDouble(b -> b.getDownPayment() != null ? b.getDownPayment() : 0.0)
                        .sum();
                double nextDueAmount = myBookings.stream()
                        .filter(b -> b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.PENDING)
                        .mapToDouble(b -> b.getMonthlyPayment() != null ? b.getMonthlyPayment() : 0.0)
                        .findFirst()
                        .orElse(0.0);

                model.addAttribute("totalPropertyValue", totalPropertyValue);
                model.addAttribute("totalPaid", totalDownPaid);
                model.addAttribute("nextDueAmount", nextDueAmount);

                List<ChatService.ConversationSummary> conversations = chatService.getUserConversations(buyer);
                model.addAttribute("conversations", conversations);
                model.addAttribute("unreadMessageCount", chatService.getUnreadMessageCount(buyer));

                List<Notification> notifications = notificationService.getUserNotifications(buyer);
                model.addAttribute("notifications", notifications);
                model.addAttribute("unreadCount", notificationService.getUnreadCount(buyer));
            }
        }
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

                List<Booking> sellerBookings = bookingService.getSellerBookings(seller);
                model.addAttribute("sellerBookings", sellerBookings);
                long pendingBookingsCount = sellerBookings.stream()
                        .filter(b -> b.getStatus() == BookingStatus.PENDING)
                        .count();
                model.addAttribute("pendingBookingsCount", pendingBookingsCount);

                List<Notification> notifications = notificationService.getUserNotifications(seller);
                model.addAttribute("notifications", notifications);

                long unreadCount = notificationService.getUnreadCount(seller);
                model.addAttribute("unreadCount", unreadCount);

                List<ChatService.ConversationSummary> conversations = chatService.getUserConversations(seller);
                model.addAttribute("conversations", conversations);

                long unreadMessageCount = chatService.getUnreadMessageCount(seller);
                model.addAttribute("unreadMessageCount", unreadMessageCount);
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
