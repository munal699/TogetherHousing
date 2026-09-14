package org.example.togetherhousing.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
@Data
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // User who sends the message
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserTbl sender;

    // User who receives the message
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserTbl receiver;

    // Actual message
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    // When the message was sent
    @Column(nullable = false)
    private LocalDateTime sentAt;

    // Whether receiver has read the message
    @Column(nullable = false)
    private boolean isRead = false;

    public ChatMessage() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserTbl getSender() {
        return sender;
    }

    public void setSender(UserTbl sender) {
        this.sender = sender;
    }

    public UserTbl getReceiver() {
        return receiver;
    }

    public void setReceiver(UserTbl receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}