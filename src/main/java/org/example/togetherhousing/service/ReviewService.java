package org.example.togetherhousing.service;

import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.model.Review;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.PropertyRepository;
import org.example.togetherhousing.repository.ReviewRepository;
import org.example.togetherhousing.repository.userRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;
    private final userRepository uRepo;
    private final NotificationService notificationService;

    public ReviewService(ReviewRepository reviewRepository,
                         PropertyRepository propertyRepository,
                         userRepository uRepo,
                         NotificationService notificationService) {
        this.reviewRepository = reviewRepository;
        this.propertyRepository = propertyRepository;
        this.uRepo = uRepo;
        this.notificationService = notificationService;
    }

    @Transactional
    public Review addReview(UserTbl buyer, Integer propertyId, Integer sellerId, Integer rating, String comment) {
        if (buyer == null) {
            throw new IllegalArgumentException("You must be logged in to submit a review.");
        }

        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5 stars.");
        }

        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("Review comment cannot be empty.");
        }

        Property property = null;
        if (propertyId != null) {
            property = propertyRepository.findById(propertyId).orElse(null);
        }

        UserTbl seller = null;
        if (sellerId != null) {
            seller = uRepo.findById(sellerId).orElse(null);
        }
        if (seller == null && property != null) {
            seller = property.getSeller();
        }

        if (seller == null) {
            throw new IllegalArgumentException("Cannot identify the seller to review.");
        }

        if (seller.getId().equals(buyer.getId())) {
            throw new IllegalArgumentException("You cannot review your own property or profile.");
        }

        Review review = new Review();
        review.setBuyer(buyer);
        review.setSeller(seller);
        review.setProperty(property);
        review.setRating(rating);
        review.setComment(comment.trim());
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);

        // Notify the seller
        String shortComment = comment.length() > 60 ? comment.substring(0, 57) + "..." : comment;
        notificationService.sendNotification(
                seller,
                property,
                String.format("New %d-star review from %s: \"%s\"", rating, buyer.getFullName(), shortComment),
                "NEW_REVIEW"
        );

        return saved;
    }

    public List<Review> getPropertyReviews(Property property) {
        return reviewRepository.findByPropertyOrderByCreatedAtDesc(property);
    }

    public List<Review> getSellerReviews(UserTbl seller) {
        return reviewRepository.findBySellerOrderByCreatedAtDesc(seller);
    }

    public double getSellerAverageRating(UserTbl seller) {
        Double avg = reviewRepository.findAverageRatingBySeller(seller);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 5.0;
    }

    public long getSellerReviewCount(UserTbl seller) {
        return reviewRepository.countBySeller(seller);
    }

    public double getPropertyAverageRating(Property property) {
        Double avg = reviewRepository.findAverageRatingByProperty(property);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 5.0;
    }

    public long getPropertyReviewCount(Property property) {
        return reviewRepository.countByProperty(property);
    }
}
