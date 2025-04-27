package com.analytics.controller;

import com.analytics.config.KafkaProducerConfig;
import com.analytics.service.AnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class AnalyticsController {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsController.class);

    public AnalyticsController() {
        logger.info("Inside Analyticcontroller");
    }
    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/event-type-counts")
    public Map<String, Integer> getEventTypeCounts() {
        logger.info("inside geteventtypecounts");
        return analyticsService.getEventTypeCounts();
    }

    @GetMapping("/events-per-minute")
    public Map<String, Integer> getEventsPerMinute() {
        logger.info("inside getEventsPerMinute");
        return analyticsService.getEventsPerMinute();
    }

    @GetMapping("/events-per-user")
    public Map<String, Integer> getEventsPerUser() {
        logger.info("inside getEventsPerUser");
        return analyticsService.getUserEventCounts();
    }

    @GetMapping("/peak-hour")
    public int getPeakHour() {
        logger.info("inside getPeakHour");
        return analyticsService.getPeakHour();
    }

    @GetMapping("/top-event-types")
    public Map<String, Long> getTopEventTypes(@RequestParam int minutes) {
        logger.info("inside getTopEventTypes");
        return analyticsService.getTopEventTypesInLastXMinutes(minutes);
    }

    @GetMapping("/events-per-last-10-minutes")
    public Map<String, Integer> getEventsLast10Minutes() {
        return analyticsService.getEventsPerLast10Minutes();
    }


}
