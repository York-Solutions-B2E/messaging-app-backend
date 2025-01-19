package com.example.messaging_app_backend.api.repositories;

import com.example.messaging_app_backend.api.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // No custom queries needed for now
}