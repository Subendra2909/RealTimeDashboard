package com.analytics.kafka;

import com.analytics.model.Event;
import com.analytics.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class EventConsumer {

    @Autowired
    //injects object of EventRepository without using new()
    private EventRepository eventRepository;

    @KafkaListener(topics = "events", groupId = "g1")
    public void listen(String json) {
        try {
            // Convert JSON string to Event object
            ObjectMapper mapper = new ObjectMapper();
            Event event = mapper.readValue(json, Event.class);

            // Save to DB
            eventRepository.save(event);

            System.out.println("Got from Kafka and saved: " + json);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Failed to parse or save event: " + json);
        }
    }
}
