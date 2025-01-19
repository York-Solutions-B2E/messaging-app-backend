package com.example.messaging_app_backend.api.kafka;

import com.example.messaging_app_backend.api.kafka.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Event> kafkaTemplate; // Updated to Event
    private final String topicName;

    public KafkaProducer(KafkaTemplate<String, Event> kafkaTemplate,
                         @Value("${spring.kafka.topic.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendEvent(Event event) { // Updated to accept Event
        System.out.println("Kafka topic name: " + topicName);
        kafkaTemplate.send(topicName, event); // Send Event object to Kafka
        System.out.println("Event sent to Kafka: " + event); // Debugging statement
    }
}