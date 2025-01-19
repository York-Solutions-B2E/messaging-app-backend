package com.example.messaging_app_backend.api.repositories;

import com.example.messaging_app_backend.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Optional: Add custom query methods if needed
    User findByName(String name);
}