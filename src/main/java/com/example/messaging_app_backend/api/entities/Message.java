package com.example.messaging_app_backend.api.entities;

import com.example.messaging_app_backend.api.enums.MessageStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String messageBody;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // Add this annotation
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING) // Store enum as a string in the database
    @Column(nullable = false)
    private MessageStatus status;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(); // Correct placement
    }

    public Message(Long id, String messageBody, Long senderId, Long receiverId, LocalDateTime createdAt, MessageStatus status) {
        this.id = id;
        this.messageBody = messageBody;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.createdAt = createdAt;
        this.status = status;
    }

    // No-args constructor for JPA
    public Message() {}


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}