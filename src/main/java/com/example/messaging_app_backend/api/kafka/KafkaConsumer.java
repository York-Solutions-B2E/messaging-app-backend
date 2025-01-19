package com.example.messaging_app_backend.api.kafka;

import com.example.messaging_app_backend.api.entities.Message;
import com.example.messaging_app_backend.api.enums.MessageStatus;
import com.example.messaging_app_backend.api.repositories.MessageRepository;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private final MessageRepository messageRepository;

    public KafkaConsumer(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @KafkaListener(topics = "my-topic", groupId = "messaging-group")
    public void listen(ConsumerRecord<String, Event> record) {
        Event event = record.value();
        System.out.println("Received event: " + event);

        Message message = new Message();
        message.setMessageBody(event.getMessage());
        message.setCreatedAt(event.getTimestamp());
        message.setSenderId(event.getUserId());
        message.setStatus(MessageStatus.DELIVERED);
        message.setReceiverId(1l);

        messageRepository.save(message);



        // Additional processing logic can go here
    }
    @PostConstruct
    public void init() {
        System.out.println("KafkaConsumer initialized and ready to listen!");
    }
}