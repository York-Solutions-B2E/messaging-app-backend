package com.example.messaging_app_backend.api.enums;

public enum MessageStatus {
    SENT,        // Message has been sent
    DELIVERED,   // Message has been delivered to the recipient
    READ,        // Message has been read by the recipient
    DELETED      // Message is marked as deleted (no longer visible)
}