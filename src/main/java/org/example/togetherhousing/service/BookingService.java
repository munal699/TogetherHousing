package org.example.togetherhousing.service;

import org.example.togetherhousing.model.Booking;
import org.example.togetherhousing.model.BookingStatus;
import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;

    public BookingService(BookingRepository bookingRepository,
                          NotificationService notificationService,
                          EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    @Transactional
    public Booking createBooking(UserTbl buyer, Property property, Double downPayment, Integer durationMonths, String notes) {
        if (property == null || buyer == null) {
            throw new IllegalArgumentException("Property and Buyer must not be null.");
        }

        if (property.getSeller() != null && property.getSeller().getId().equals(buyer.getId())) {
            throw new IllegalArgumentException("You cannot book your own property.");
        }

        if (durationMonths == null || durationMonths <= 0) {
            durationMonths = 24;
        }

        double minDown = property.getDownPayment() != null ? property.getDownPayment() : 0.0;
        double actualDown = (downPayment != null && downPayment >= 0) ? downPayment : minDown;
        double totalPrice = property.getPrice() != null ? property.getPrice() : 0.0;
        double remaining = Math.max(0.0, totalPrice - actualDown);
        double monthlyPayment = durationMonths > 0 ? (remaining / durationMonths) : 0.0;

        Booking booking = new Booking();
        booking.setBuyer(buyer);
        booking.setProperty(property);
        booking.setTotalPrice(totalPrice);
        booking.setDownPayment(actualDown);
        booking.setDurationMonths(durationMonths);
        booking.setMonthlyPayment(monthlyPayment);
        booking.setNotes(notes);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        Booking saved = bookingRepository.save(booking);

        // 1. In-App Notifications
        if (property.getSeller() != null) {
            notificationService.sendNotification(
                    property.getSeller(),
                    property,
                    String.format("New booking request from %s for property '%s'.", buyer.getFullName(), property.getTitle()),
                    "BOOKING_REQUEST"
            );
        }
        notificationService.sendNotification(
                buyer,
                property,
                String.format("Your booking request for '%s' was submitted successfully.", property.getTitle()),
                "BOOKING_PENDING"
        );

        // 2. SMTP Notifications
        String sellerEmail = property.getSeller() != null ? property.getSeller().getEmail() : null;
        String sellerName = property.getSeller() != null ? property.getSeller().getFullName() : "Seller";
        emailService.sendBookingCreatedNotification(
                buyer.getEmail(),
                buyer.getFullName(),
                sellerEmail,
                sellerName,
                property.getTitle(),
                actualDown,
                monthlyPayment
        );

        return saved;
    }

    @Transactional
    public Booking confirmBooking(Integer bookingId, UserTbl seller) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

        if (seller != null && booking.getProperty().getSeller() != null) {
            if (!booking.getProperty().getSeller().getId().equals(seller.getId())) {
                throw new SecurityException("Unauthorized: You do not own this property.");
            }
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        Booking updated = bookingRepository.save(booking);

        // In-app notification
        notificationService.sendNotification(
                booking.getBuyer(),
                booking.getProperty(),
                String.format("Great news! Your booking for '%s' has been confirmed by the owner.", booking.getProperty().getTitle()),
                "BOOKING_CONFIRMED"
        );

        // SMTP notification
        emailService.sendBookingStatusUpdate(
                booking.getBuyer().getEmail(),
                booking.getBuyer().getFullName(),
                booking.getProperty().getTitle(),
                "CONFIRMED",
                "Your reservation has been officially approved."
        );

        return updated;
    }

    @Transactional
    public Booking rejectBooking(Integer bookingId, UserTbl seller, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

        if (seller != null && booking.getProperty().getSeller() != null) {
            if (!booking.getProperty().getSeller().getId().equals(seller.getId())) {
                throw new SecurityException("Unauthorized: You do not own this property.");
            }
        }

        booking.setStatus(BookingStatus.REJECTED);
        booking.setRejectionReason(reason);
        Booking updated = bookingRepository.save(booking);

        // In-app notification
        notificationService.sendNotification(
                booking.getBuyer(),
                booking.getProperty(),
                String.format("Booking update: Request for '%s' was rejected. Reason: %s",
                        booking.getProperty().getTitle(), (reason != null && !reason.isBlank()) ? reason : "Not specified"),
                "BOOKING_REJECTED"
        );

        // SMTP notification
        emailService.sendBookingStatusUpdate(
                booking.getBuyer().getEmail(),
                booking.getBuyer().getFullName(),
                booking.getProperty().getTitle(),
                "REJECTED",
                reason
        );

        return updated;
    }

    @Transactional
    public Booking cancelBooking(Integer bookingId, UserTbl buyer) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

        if (buyer != null && !booking.getBuyer().getId().equals(buyer.getId())) {
            throw new SecurityException("Unauthorized: You can only cancel your own booking.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking updated = bookingRepository.save(booking);

        // Notify seller if present
        if (booking.getProperty().getSeller() != null) {
            notificationService.sendNotification(
                    booking.getProperty().getSeller(),
                    booking.getProperty(),
                    String.format("Booking for '%s' was cancelled by buyer %s.",
                            booking.getProperty().getTitle(), booking.getBuyer().getFullName()),
                    "BOOKING_CANCELLED"
            );

            // SMTP notification to seller
            if (booking.getProperty().getSeller().getEmail() != null) {
                emailService.sendEmail(
                        booking.getProperty().getSeller().getEmail(),
                        "Booking Request Cancelled: " + booking.getProperty().getTitle(),
                        String.format("Hello %s,\n\nThe booking request for '%s' was cancelled by buyer %s.\n\nBest regards,\nTogether Housing Team",
                                booking.getProperty().getSeller().getFullName(),
                                booking.getProperty().getTitle(),
                                booking.getBuyer().getFullName())
                );
            }
        }

        // SMTP notification to buyer
        if (booking.getBuyer() != null && booking.getBuyer().getEmail() != null) {
            emailService.sendEmail(
                    booking.getBuyer().getEmail(),
                    "Booking Cancellation Confirmed: " + booking.getProperty().getTitle(),
                    String.format("Hello %s,\n\nYour booking reservation for '%s' has been successfully cancelled.\n\nBest regards,\nTogether Housing Team",
                            booking.getBuyer().getFullName(),
                            booking.getProperty().getTitle())
            );
        }

        return updated;
    }

    public List<Booking> getBuyerBookings(UserTbl buyer) {
        return bookingRepository.findByBuyerOrderByCreatedAtDesc(buyer);
    }

    public List<Booking> getSellerBookings(UserTbl seller) {
        return bookingRepository.findByPropertySellerOrderByCreatedAtDesc(seller);
    }

    public Optional<Booking> getBookingById(Integer id) {
        return bookingRepository.findById(id);
    }

    public boolean hasActiveBooking(UserTbl buyer, Property property) {
        return !bookingRepository.findActiveBooking(buyer, property).isEmpty();
    }
}
