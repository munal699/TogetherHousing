package org.example.togetherhousing.service;

import org.example.togetherhousing.model.Notification;
import org.example.togetherhousing.model.Property;
import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void sendNotification(UserTbl user, Property property, String message, String type) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setProperty(property);
        notification.setMessage(message);
        notification.setType(type);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notificationRepository.save(notification);
    }

    public List<Notification> getUserNotifications(UserTbl user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Notification> getUnreadNotifications(UserTbl user) {
        return notificationRepository.findByUserAndReadFalse(user);
    }

    public long getUnreadCount(UserTbl user) {
        return notificationRepository.countByUserAndReadFalse(user);
    }

    public void markAsRead(Integer notificationId) {
        Notification n = notificationRepository.findById(notificationId).orElse(null);
        if (n != null) {
            n.setRead(true);
            notificationRepository.save(n);
        }
    }

    public void markAllAsRead(UserTbl user) {
        List<Notification> unread = notificationRepository.findByUserAndReadFalse(user);
        for (Notification n : unread) {
            n.setRead(true);
        }
        notificationRepository.saveAll(unread);
    }
}
