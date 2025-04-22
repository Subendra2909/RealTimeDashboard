package com.analytics.controller;

import java.util.List;

import com.analytics.model.Event;
import com.analytics.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    //Accepts a JSON payload from the client
    //Maps it to an Event object (@RequestBody)
    //Saves it to the database using save()
    //Returns the saved object (with id and timestamp)

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        Event savedEvent = eventRepository.save(event);
        return new ResponseEntity<>(savedEvent,HttpStatus.CREATED);
    }


    //When someone calls /api/events via a GET request
    //It fetches all rows in the event table using findAll()
    //Returns the full list as JSON

    @GetMapping
    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }
}
