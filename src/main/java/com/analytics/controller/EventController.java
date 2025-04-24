package com.analytics.controller;

import java.util.List;

import com.analytics.model.Event;
import com.analytics.repository.EventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    //injects object of EventRepository without using new()
    private EventRepository eventRepository;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;


    //Accepts a JSON payload from the client
    //Maps it to an Event object (@RequestBody)
    //Saves it to the database using save()
    //Returns the saved object (with id and timestamp)

    @PostMapping
    public ResponseEntity<String> createEvent(@Valid @RequestBody Event event) throws JsonProcessingException {
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
        return eventRepository.findAll();
    }
}
