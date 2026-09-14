package org.example.togetherhousing.repository;

import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.model.PropertyStatus;
import org.example.togetherhousing.model.UserTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {

    List<Property> findByStatus(PropertyStatus status);

    List<Property> findBySeller(UserTbl seller);

    List<Property> findBySellerAndStatus(UserTbl seller, PropertyStatus status);
}
