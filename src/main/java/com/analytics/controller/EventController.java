package com.analytics.controller;

import java.util.List;

import com.analytics.model.Event;
import com.analytics.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Event createEvent(@RequestBody Event event){
        return eventRepository.save(event);
    }


    //When someone calls /api/events via a GET request
    //It fetches all rows in the event table using findAll()
    //Returns the full list as JSON

    @GetMapping
    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }
}
