package com.analytics.kafka;

import com.analytics.controller.EventController;
import com.analytics.model.Event;
import com.analytics.repository.EventRepository;
import com.analytics.service.AnalyticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class EventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    public EventConsumer() {
        logger.info("Inside EventConsumer");
    }

    @Autowired
    //injects object of EventRepository without using new()
    private EventRepository eventRepository;

    @Autowired
    private AnalyticsService analyticsService;

    @KafkaListener(topics = "events", groupId = "g1", concurrency = "5")
    public void listen(String json) {
        logger.info("Inside kafkalistener listen");
        try {
            // Convert JSON string to Event object
            ObjectMapper mapper = new ObjectMapper();
            Event event = mapper.readValue(json, Event.class);

            analyticsService.processEvent(event);

            // Save to DB
            long start = System.currentTimeMillis();
            eventRepository.save(event);
            long end = System.currentTimeMillis();
            System.out.println("DB insert took: " + (end - start) + "ms");

            System.out.println("Got from Kafka and saved: " + json);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Failed to parse or save event: " + json);
        }
    }
}
