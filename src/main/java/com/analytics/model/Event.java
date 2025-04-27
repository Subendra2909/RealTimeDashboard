package com.analytics.model;

import com.analytics.kafka.EventConsumer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.datatype.jsr310.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
public class Event {
    private static final Logger logger = LoggerFactory.getLogger(Event.class);

    public Event() {
        logger.info("Inside Event");
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "User ID cannot be blank")
    private String userId;

    @NotBlank(message = "Event type cannot be blank")
    private String eventType;

    @NotBlank(message = "Page URL cannot be blank")
    private String pageUrl;

    @NotBlank(message = "Timestamp is required")
    @Column(length = 50)             // optional: ensure enough space
    private String timestamp;

    public Long getId() { return id; }
    public void setId(Long id) {this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getPageUrl() { return pageUrl; }
    public void setPageUrl(String pageUrl) { this.pageUrl = pageUrl; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
