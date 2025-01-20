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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public MessageDTO deleteMessage(Long id) {
        // Step 1: Find the message by ID
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message with id " + id + " not found"));

        // Step 2: Update the message status to 'DELETED'
        message.setStatus(MessageStatus.DELETED);
        messageRepository.save(message); // Save the updated status to the database

        // Step 3: Create the event to send to Kafka
        Event event = new Event();
        event.setId(message.getId());
        event.setMessage(message.getMessageBody()); // Include the message text if needed
        event.setTimestamp(LocalDateTime.now());
        event.setUsername("john doe");
        event.setUserId(message.getSenderId());
        event.setStatus("DELETED");

        // Step 4: Send the event to Kafka
        kafkaProducer.sendEvent(event);

        // Step 5: Map the updated message entity to a DTO and return it
        return messageMapper.toDTO(message);
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

        // Validate and update the message body
        if (updatedMessageDTO.getMessageBody() != null && !updatedMessageDTO.getMessageBody().isBlank()) {
            existingMessage.setMessageBody(updatedMessageDTO.getMessageBody());
        } else {
            throw new IllegalArgumentException("Message body cannot be null or blank.");
        }

        // Save the updated message to the database
        Message savedMessage = messageRepository.save(existingMessage);

        // Optionally send an update event to Kafka
        Event updateEvent = new Event();
        updateEvent.setId(savedMessage.getId());
        updateEvent.setMessage(savedMessage.getMessageBody());
        updateEvent.setStatus("UPDATED");
        updateEvent.setUserId(savedMessage.getSenderId());
        updateEvent.setTimestamp(savedMessage.getCreatedAt());
        kafkaProducer.sendEvent(updateEvent);
        System.out.println("Sending UPDATED event to Kafka: " + updateEvent);

        // Convert the updated Entity back to DTO and return
        return messageMapper.toDTO(savedMessage);
    }


}