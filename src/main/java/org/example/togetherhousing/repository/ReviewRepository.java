package org.example.togetherhousing.repository;

import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.model.Review;
import org.example.togetherhousing.model.UserTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findBySellerOrderByCreatedAtDesc(UserTbl seller);

    List<Review> findByPropertyOrderByCreatedAtDesc(Property property);

    long countBySeller(UserTbl seller);

    long countByProperty(Property property);

    @Query("SELECT COALESCE(AVG(r.rating), 5.0) FROM Review r WHERE r.seller = :seller")
    Double findAverageRatingBySeller(@Param("seller") UserTbl seller);

    @Query("SELECT COALESCE(AVG(r.rating), 5.0) FROM Review r WHERE r.property = :property")
    Double findAverageRatingByProperty(@Param("property") Property property);
}
