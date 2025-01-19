package com.example.messaging_app_backend.api.services.impl;

import com.example.messaging_app_backend.api.DTO.MessageDTO;
import com.example.messaging_app_backend.api.entities.Message;
import com.example.messaging_app_backend.api.enums.MessageStatus;
import com.example.messaging_app_backend.api.kafka.Event;
import com.example.messaging_app_backend.api.mappers.MessageMapper;
import com.example.messaging_app_backend.api.repositories.MessageRepository;
import com.example.messaging_app_backend.api.services.MessageService;
import com.example.messaging_app_backend.api.kafka.KafkaProducer; // Import the Kafka producer
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper; // Inject the custom ObjectMapper

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository,
                              MessageMapper messageMapper,
                              KafkaProducer kafkaProducer,
                              ObjectMapper objectMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper; // Use the custom ObjectMapper
    }

    @Override
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        // Convert DTO to Entity
        Message message = messageMapper.toEntity(messageDTO);

        // Set the default status to SENT
        message.setStatus(MessageStatus.SENT);

        // Save the message to the database
        Message savedMessage = messageRepository.save(message);

        // Convert the saved Message object to JSON for Kafka
        String messageJson = convertMessageToJson(savedMessage);
        Event event = new Event();
        event.setMessage(message.getMessageBody());
        event.setId(message.getId());
        event.setStatus("SENT");
        event.setUserId(message.getSenderId());
        event.setTimestamp(message.getCreatedAt());
        // Send the serialized message to Kafka
        kafkaProducer.sendEvent(event);

        // Convert the saved Entity back to DTO and return
        return messageMapper.toDTO(savedMessage);
    }

    // Utility method to convert Message object to JSON
    private String convertMessageToJson(Message message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert message to JSON", e);
        }
    }

    @Override
    public List<MessageDTO> getAllMessages() {
        // Fetch all messages and convert them to DTOs
        List<Message> messages = messageRepository.findAll();
        return messages.stream()
                .map(messageMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MessageDTO updateMessage(Long id, MessageDTO updatedMessageDTO) {
        // Find the message by ID
        Optional<Message> optionalMessage = messageRepository.findById(id);

        if (optionalMessage.isEmpty()) {
            throw new IllegalArgumentException("Message with ID " + id + " not found.");
        }

        Message existingMessage = optionalMessage.get();

        // Update fields if new values are provided
        if (updatedMessageDTO.getMessageBody() != null) {
            existingMessage.setMessageBody(updatedMessageDTO.getMessageBody());
        }
        if (updatedMessageDTO.getSenderId() != null) {
            existingMessage.setSenderId(updatedMessageDTO.getSenderId());
        }
        if (updatedMessageDTO.getReceiverId() != null) {
            existingMessage.setReceiverId(updatedMessageDTO.getReceiverId());
        }

        // Save the updated message
        Message savedMessage = messageRepository.save(existingMessage);

        // Convert the updated message to JSON and send to Kafka
        String messageJson = convertMessageToJson(savedMessage);
//        kafkaProducer.sendEvent("my-topic", messageJson);

        // Convert the updated Entity back to DTO and return
        return messageMapper.toDTO(savedMessage);
    }
}