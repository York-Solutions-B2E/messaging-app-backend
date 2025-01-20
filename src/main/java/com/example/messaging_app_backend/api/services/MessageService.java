package com.example.messaging_app_backend.api.services;

import com.example.messaging_app_backend.api.DTO.MessageDTO;
import com.example.messaging_app_backend.api.entities.Message;

import java.util.List;

public interface MessageService {
    MessageDTO sendMessage(MessageDTO messageDTO);

    MessageDTO deleteMessage(Long id);

    List<MessageDTO> getAllMessages();
    MessageDTO updateMessage(Long id, MessageDTO updatedMessageDTO);
}