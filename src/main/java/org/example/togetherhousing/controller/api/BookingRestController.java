package org.example.togetherhousing.controller.api;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.togetherhousing.dto.ApiResponse;
import org.example.togetherhousing.dto.BookingRequestDto;
import org.example.togetherhousing.model.Booking;
import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.PropertyRepository;
import org.example.togetherhousing.repository.userRepository;
import org.example.togetherhousing.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingRestController {

    private final BookingService bookingService;
    private final PropertyRepository propertyRepository;
    private final userRepository uRepo;

    public BookingRestController(BookingService bookingService,
                                 PropertyRepository propertyRepository,
                                 userRepository uRepo) {
        this.bookingService = bookingService;
        this.propertyRepository = propertyRepository;
        this.uRepo = uRepo;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Booking>> createBooking(@Valid @RequestBody BookingRequestDto dto,
                                                              HttpSession session) {
        UserTbl buyer = null;
        if (dto.getBuyerId() != null) {
            buyer = uRepo.findById(dto.getBuyerId()).orElse(null);
        } else {
            String email = (String) session.getAttribute("email");
            if (email != null) {
                buyer = uRepo.findByEmail(email).orElse(null);
            }
        }

        if (buyer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Authentication required: provide buyerId in body or log in to session."));
        }

        Property property = propertyRepository.findById(dto.getPropertyId()).orElse(null);
        if (property == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Property not found with id: " + dto.getPropertyId()));
        }

        try {
            Booking booking = bookingService.createBooking(
                    buyer,
                    property,
                    dto.getDownPayment(),
                    dto.getDurationMonths(),
                    dto.getNotes()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Booking created successfully", booking));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Booking>> getBookingById(@PathVariable("id") Integer id) {
        return bookingService.getBookingById(id)
                .map(b -> ResponseEntity.ok(ApiResponse.ok("Booking retrieved", b)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Booking not found with id: " + id)));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<Booking>> confirmBooking(@PathVariable("id") Integer id,
                                                               @RequestParam(name = "sellerId", required = false) Integer sellerId,
                                                               HttpSession session) {
        UserTbl seller = null;
        if (sellerId != null) {
            seller = uRepo.findById(sellerId).orElse(null);
        } else {
            String email = (String) session.getAttribute("email");
            if (email != null) seller = uRepo.findByEmail(email).orElse(null);
        }

        try {
            Booking updated = bookingService.confirmBooking(id, seller);
            return ResponseEntity.ok(ApiResponse.ok("Booking confirmed successfully", updated));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<Booking>> rejectBooking(@PathVariable("id") Integer id,
                                                              @RequestBody(required = false) Map<String, String> body,
                                                              @RequestParam(name = "sellerId", required = false) Integer sellerId,
                                                              HttpSession session) {
        UserTbl seller = null;
        if (sellerId != null) {
            seller = uRepo.findById(sellerId).orElse(null);
        } else {
            String email = (String) session.getAttribute("email");
            if (email != null) seller = uRepo.findByEmail(email).orElse(null);
        }

        String reason = body != null ? body.get("reason") : null;
        try {
            Booking updated = bookingService.rejectBooking(id, seller, reason);
            return ResponseEntity.ok(ApiResponse.ok("Booking rejected successfully", updated));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Booking>> cancelBooking(@PathVariable("id") Integer id,
                                                              @RequestParam(name = "buyerId", required = false) Integer buyerId,
                                                              HttpSession session) {
        UserTbl buyer = null;
        if (buyerId != null) {
            buyer = uRepo.findById(buyerId).orElse(null);
        } else {
            String email = (String) session.getAttribute("email");
            if (email != null) buyer = uRepo.findByEmail(email).orElse(null);
        }

        try {
            Booking updated = bookingService.cancelBooking(id, buyer);
            return ResponseEntity.ok(ApiResponse.ok("Booking cancelled successfully", updated));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }
}
