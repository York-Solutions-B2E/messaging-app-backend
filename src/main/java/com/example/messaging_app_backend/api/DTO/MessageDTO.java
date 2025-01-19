package com.example.messaging_app_backend.api.DTO;

import com.example.messaging_app_backend.api.enums.MessageStatus; // Import the MessageStatus enum
import java.time.LocalDateTime; // Import LocalDateTime

public class MessageDTO {
    private Long id;
    private String messageBody;
    private Long senderId;
    private Long receiverId;
    private MessageStatus status; // Add the status field
    private LocalDateTime createdAt; // Add the createdAt field

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
        return status; // Getter for status
    }

    public void setStatus(MessageStatus status) {
        this.status = status; // Setter for status
    }

    public LocalDateTime getCreatedAt() {
        return createdAt; // Getter for createdAt
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt; // Setter for createdAt
    }
}