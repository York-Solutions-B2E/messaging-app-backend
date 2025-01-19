
package com.example.messaging_app_backend.api.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    // ObjectMapper with JavaTimeModule for handling LocalDateTime
    @Bean("kafkaJacksonConfig")
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Register JavaTimeModule to handle LocalDateTime in ISO-8601 format
        objectMapper.registerModule(new JavaTimeModule());

        // Register custom deserializer for LocalDateTime to handle both ISO-8601 and Unix timestamps
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        objectMapper.registerModule(module);

        // Disable writing dates as timestamps
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }

    // Custom Deserializer for LocalDateTime to handle Unix timestamp and ISO-8601 format
    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.getCurrentToken().isScalarValue()) {
                String value = p.getValueAsString();

                try {
                    // Try parsing as Unix timestamp (in milliseconds)
                    long timestamp = Long.parseLong(value);
                    return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
                } catch (NumberFormatException e) {
                    // Fall back to ISO-8601 format if it's not a valid Unix timestamp
                    return LocalDateTime.parse(value);
                }
            }
            return null;
        }
    }
}
