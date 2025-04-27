package com.analytics.controller;

import java.util.List;
import java.util.Map;

import com.analytics.config.KafkaProducerConfig;
import com.analytics.model.Event;
import com.analytics.repository.EventRepository;
import com.analytics.service.AnalyticsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/events")
public class EventController {
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    public EventController() {
        logger.info("Inside EventController");
    }

    @Autowired
    //injects object of EventRepository without using new()
    private EventRepository eventRepository;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private AnalyticsService analyticsService;


    //Accepts a JSON payload from the client
    //Maps it to an Event object (@RequestBody)
    //Saves it to the database using save()
    //Returns the saved object (with id and timestamp)

    @PostMapping
    public ResponseEntity<String> createEvent(@Valid @RequestBody Event event) throws JsonProcessingException {
        logger.info("Inside createEvent");
        // Convert event object to JSON string
        String eventJson = new ObjectMapper().writeValueAsString(event);

        // Send to Kafka
        kafkaTemplate.send("events",eventJson);

        return ResponseEntity.ok("Event sent to Kafka");
    }


    //When someone calls /api/events via a GET request
    //It fetches all rows in the event table using findAll()
    //Returns the full list as JSON

    @GetMapping
    public List<Event> getAllEvents(){
        logger.info("Inside getAllEvents");
        return eventRepository.findAll();
    }

}
