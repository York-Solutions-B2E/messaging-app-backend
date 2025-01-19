package com.example.messaging_app_backend.api.mappers;

import com.example.messaging_app_backend.api.entities.Message;
import com.example.messaging_app_backend.api.DTO.MessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "id", ignore = true) // Auto-generated ID
    @Mapping(target = "senderId", source = "senderId") // Map senderId directly
    @Mapping(target = "receiverId", source = "receiverId") // Map receiverId directly
    @Mapping(target = "status", source = "status") // Map MessageStatus enum
    @Mapping(target = "createdAt", source = "createdAt") // Map createdAt timestamp
    Message toEntity(MessageDTO dto);

    @Mapping(target = "status", source = "status") // Map MessageStatus enum for DTO
    @Mapping(target = "createdAt", source = "createdAt") // Map createdAt timestamp
    MessageDTO toDTO(Message entity);
}