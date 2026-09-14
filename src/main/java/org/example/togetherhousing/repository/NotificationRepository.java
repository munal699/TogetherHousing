package org.example.togetherhousing.repository;

import org.example.togetherhousing.model.Notification;
import org.example.togetherhousing.model.UserTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserOrderByCreatedAtDesc(UserTbl user);
    List<Notification> findByUserAndReadFalse(UserTbl user);
    long countByUserAndReadFalse(UserTbl user);
}
