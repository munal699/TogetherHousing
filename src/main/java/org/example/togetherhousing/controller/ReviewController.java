package org.example.togetherhousing.controller;

import jakarta.servlet.http.HttpSession;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.userRepository;
import org.example.togetherhousing.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final userRepository uRepo;

    public ReviewController(ReviewService reviewService, userRepository uRepo) {
        this.reviewService = reviewService;
        this.uRepo = uRepo;
    }

    @PostMapping("/review/add")
    public String addReview(@RequestParam(value = "propertyId", required = false) Integer propertyId,
                            @RequestParam(value = "sellerId", required = false) Integer sellerId,
                            @RequestParam("rating") Integer rating,
                            @RequestParam("comment") String comment,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            redirectAttributes.addFlashAttribute("error", "Please sign in to submit a review.");
            return "redirect:/login";
        }

        UserTbl buyer = uRepo.findByEmail(email).orElse(null);
        if (buyer == null) {
            return "redirect:/login";
        }

        try {
            reviewService.addReview(buyer, propertyId, sellerId, rating, comment);
            redirectAttributes.addFlashAttribute("success", "Thank you! Your review and rating have been posted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        if (propertyId != null) {
            return "redirect:/property-detail?id=" + propertyId;
        }
        return "redirect:/properties";
    }
}
