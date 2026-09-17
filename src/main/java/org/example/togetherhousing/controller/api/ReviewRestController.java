package org.example.togetherhousing.controller.api;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.togetherhousing.dto.ApiResponse;
import org.example.togetherhousing.dto.ReviewRequestDto;
import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.model.Review;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.PropertyRepository;
import org.example.togetherhousing.repository.userRepository;
import org.example.togetherhousing.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewRestController {

    private final ReviewService reviewService;
    private final PropertyRepository propertyRepository;
    private final userRepository uRepo;

    public ReviewRestController(ReviewService reviewService,
                                PropertyRepository propertyRepository,
                                userRepository uRepo) {
        this.reviewService = reviewService;
        this.propertyRepository = propertyRepository;
        this.uRepo = uRepo;
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPropertyReviews(@PathVariable("propertyId") Integer propertyId) {
        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Property not found with id: " + propertyId));
        }

        List<Review> reviews = reviewService.getPropertyReviews(property);
        double avg = reviewService.getPropertyAverageRating(property);

        Map<String, Object> data = new HashMap<>();
        data.put("propertyId", propertyId);
        data.put("averageRating", avg);
        data.put("totalReviews", reviews.size());
        data.put("reviews", reviews);

        return ResponseEntity.ok(ApiResponse.ok("Property reviews retrieved", data));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSellerReviews(@PathVariable("sellerId") Integer sellerId) {
        UserTbl seller = uRepo.findById(sellerId).orElse(null);
        if (seller == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Seller not found with id: " + sellerId));
        }

        List<Review> reviews = reviewService.getSellerReviews(seller);
        double avg = reviewService.getSellerAverageRating(seller);

        Map<String, Object> data = new HashMap<>();
        data.put("sellerId", sellerId);
        data.put("sellerName", seller.getFullname());
        data.put("averageRating", avg);
        data.put("totalReviews", reviews.size());
        data.put("reviews", reviews);

        return ResponseEntity.ok(ApiResponse.ok("Seller reviews retrieved", data));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Review>> addReview(@Valid @RequestBody ReviewRequestDto dto,
                                                         HttpSession session) {
        UserTbl buyer = null;
        if (dto.getBuyerId() != null) {
            buyer = uRepo.findById(dto.getBuyerId()).orElse(null);
        } else {
            String email = (String) session.getAttribute("email");
            if (email != null) buyer = uRepo.findByEmail(email).orElse(null);
        }

        if (buyer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Authentication required to post a review."));
        }

        try {
            Review review = reviewService.addReview(
                    buyer,
                    dto.getPropertyId(),
                    dto.getSellerId(),
                    dto.getRating(),
                    dto.getComment()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Review submitted successfully", review));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }
}
