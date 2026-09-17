package org.example.togetherhousing.repository;

import org.example.togetherhousing.model.Booking;
import org.example.togetherhousing.model.BookingStatus;
import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.model.UserTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByBuyerOrderByCreatedAtDesc(UserTbl buyer);

    List<Booking> findByPropertySellerOrderByCreatedAtDesc(UserTbl seller);

    long countByBuyer(UserTbl buyer);

    long countByPropertySellerAndStatus(UserTbl seller, BookingStatus status);

    boolean existsByBuyerAndPropertyAndStatus(UserTbl buyer, Property property, BookingStatus status);

    List<Booking> findByProperty(Property property);

    @Query("SELECT b FROM Booking b WHERE b.buyer = :buyer AND b.property = :property AND (b.status = 'PENDING' OR b.status = 'CONFIRMED')")
    List<Booking> findActiveBooking(@Param("buyer") UserTbl buyer, @Param("property") Property property);
}
