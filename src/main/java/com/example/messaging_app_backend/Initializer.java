package com.example.messaging_app_backend;

import com.example.messaging_app_backend.api.entities.Message;
import com.example.messaging_app_backend.api.entities.User;
import com.example.messaging_app_backend.api.enums.MessageStatus;
import com.example.messaging_app_backend.api.repositories.MessageRepository;
import com.example.messaging_app_backend.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Initializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public Initializer(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void run(String... args) {
        // Create users
        User user1 = new User(null, "John Doe");
        User user2 = new User(null, "Jane Smith");

        userRepository.save(user1);
        userRepository.save(user2);

        // Create messages
        Message message1 = new Message(
                null,
                "Hello from John to Jane!",
                user1.getId(),
                user2.getId(),
                LocalDateTime.now(),
                MessageStatus.SENT
        );

        Message message2 = new Message(
                null,
                "Hi John! How are you?",
                user2.getId(),
                user1.getId(),
                LocalDateTime.now(),
                MessageStatus.SENT
        );

        Message message3 = new Message(
                null,
                "I'm doing great, Jane. Thanks for asking!",
                user1.getId(),
                user2.getId(),
                LocalDateTime.now(),
                MessageStatus.SENT
        );

        messageRepository.save(message1);
        messageRepository.save(message2);
        messageRepository.save(message3);

        System.out.println("Database has been seeded with initial user and message data.");
    }
}