package com.example.messaging_app_backend.api.controller;

import com.example.messaging_app_backend.api.DTO.MessageDTO;
import com.example.messaging_app_backend.api.services.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // POST a new message
    @PostMapping
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageDTO messageDTO) {
        MessageDTO savedMessage = messageService.sendMessage(messageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }

    @GetMapping
    public ResponseEntity<List<MessageDTO>> getAllMessages() {
        List<MessageDTO> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageDTO> updateMessage(
            @PathVariable Long id,
            @RequestBody MessageDTO updatedMessageDTO) {
        MessageDTO updatedMessage = messageService.updateMessage(id, updatedMessageDTO);
        return ResponseEntity.ok(updatedMessage);
    }
}