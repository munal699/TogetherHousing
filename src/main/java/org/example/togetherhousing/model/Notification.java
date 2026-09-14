package org.example.togetherhousing.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserTbl user;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    public Notification() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public UserTbl getUser() { return user; }
    public void setUser(UserTbl user) { this.user = user; }
    public Property getProperty() { return property; }
    public void setProperty(Property property) { this.property = property; }
}
