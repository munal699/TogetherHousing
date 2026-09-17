package org.example.togetherhousing.controller;

import jakarta.servlet.http.HttpSession;
import org.example.togetherhousing.model.Booking;
import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.PropertyRepository;
import org.example.togetherhousing.repository.userRepository;
import org.example.togetherhousing.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.Locale;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final PropertyRepository propertyRepository;
    private final userRepository uRepo;

    public BookingController(BookingService bookingService,
                             PropertyRepository propertyRepository,
                             userRepository uRepo) {
        this.bookingService = bookingService;
        this.propertyRepository = propertyRepository;
        this.uRepo = uRepo;
    }

    @GetMapping("/booking")
    public String showBookingPage(@RequestParam(name = "id", required = false) Integer propertyId,
                                  HttpSession session,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            redirectAttributes.addFlashAttribute("error", "Please sign in to book a property.");
            return "redirect:/login";
        }

        UserTbl buyer = uRepo.findByEmail(email).orElse(null);
        if (buyer == null) {
            return "redirect:/login";
        }

        Property property = null;
        if (propertyId != null) {
            property = propertyRepository.findById(propertyId).orElse(null);
        }
        if (property == null) {
            property = propertyRepository.findAll().stream().findFirst().orElse(null);
        }

        if (property == null) {
            redirectAttributes.addFlashAttribute("error", "No properties available to book.");
            return "redirect:/properties";
        }

        double price = property.getPrice() != null ? property.getPrice() : 0.0;
        double defaultDown = property.getDownPayment() != null && property.getDownPayment() > 0
                ? property.getDownPayment()
                : (price * 0.20);
        int defaultDuration = 24;
        double monthly = defaultDuration > 0 ? ((price - defaultDown) / defaultDuration) : 0.0;

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        model.addAttribute("buyer", buyer);
        model.addAttribute("property", property);
        model.addAttribute("priceFormatted", "Rs " + nf.format(price));
        model.addAttribute("downFormatted", "Rs " + nf.format(defaultDown));
        model.addAttribute("monthlyFormatted", "Rs " + nf.format(Math.round(monthly)) + " / mo");

        return "booking";
    }

    @PostMapping("/booking/create")
    public String createBooking(@RequestParam("propertyId") Integer propertyId,
                                @RequestParam(value = "downPayment", required = false) Double downPayment,
                                @RequestParam(value = "durationMonths", defaultValue = "24") Integer durationMonths,
                                @RequestParam(value = "notes", required = false) String notes,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }

        UserTbl buyer = uRepo.findByEmail(email).orElse(null);
        if (buyer == null) {
            return "redirect:/login";
        }

        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) {
            redirectAttributes.addFlashAttribute("error", "Property not found.");
            return "redirect:/properties";
        }

        try {
            bookingService.createBooking(buyer, property, downPayment, durationMonths, notes);
            redirectAttributes.addFlashAttribute("success", "You have successfully booked this property! Your reservation request has been submitted to the owner for confirmation.");
            return "redirect:/buyer-dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/booking?id=" + propertyId;
        }
    }

    @PostMapping("/seller/booking/approve")
    public String approveBooking(@RequestParam("bookingId") Integer bookingId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("email");
        if (email == null) return "redirect:/login";

        UserTbl seller = uRepo.findByEmail(email).orElse(null);
        try {
            bookingService.confirmBooking(bookingId, seller);
            redirectAttributes.addFlashAttribute("success", "Booking reservation confirmed!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/seller-dashboard";
    }

    @PostMapping("/seller/booking/reject")
    public String rejectBooking(@RequestParam("bookingId") Integer bookingId,
                                @RequestParam(value = "reason", required = false) String reason,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("email");
        if (email == null) return "redirect:/login";

        UserTbl seller = uRepo.findByEmail(email).orElse(null);
        try {
            bookingService.rejectBooking(bookingId, seller, reason);
            redirectAttributes.addFlashAttribute("success", "Booking reservation rejected.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/seller-dashboard";
    }

    @PostMapping("/booking/cancel")
    public String cancelBooking(@RequestParam("bookingId") Integer bookingId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("email");
        if (email == null) return "redirect:/login";

        UserTbl buyer = uRepo.findByEmail(email).orElse(null);
        try {
            bookingService.cancelBooking(bookingId, buyer);
            redirectAttributes.addFlashAttribute("success", "Booking has been cancelled.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/buyer-dashboard";
    }
}
