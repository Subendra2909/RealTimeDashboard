package com.analytics.service;

import com.analytics.controller.AnalyticsController;
import com.analytics.model.AnalyticsData;
import com.analytics.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);

    public AnalyticsService() {
        logger.info("Inside AnalyticsService");
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final ConcurrentHashMap<String, AtomicInteger> eventTypeCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> eventsPerMinute = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> userEventCounts = new ConcurrentHashMap<>();
    private final Map<Integer, AtomicInteger> hourlyActivity = new ConcurrentHashMap<>();
    private final List<Event> recentEvents = Collections.synchronizedList(new LinkedList<>());
    private final Map<String, AtomicInteger> minuteCounts = new ConcurrentHashMap<>();


    private static final DateTimeFormatter MINUTE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void processEvent(Event event) {
        logger.info("Inside processEvent");

        // Update your in-memory aggregations
        updateEventTypeCounts(event);
        updateUserEventCounts(event);
        updateHourlyActivity(event);
        updateRecentEvents(event);
        updateMinuteCounts(event);

        // Count per hour (0–23)
        int hour = LocalDateTime.parse(event.getTimestamp(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).getHour();
        hourlyActivity.computeIfAbsent(hour, k -> new AtomicInteger(0)).incrementAndGet();

        AnalyticsData analyticsData = buildAnalyticsData();
        messagingTemplate.convertAndSend("/topic/analytics", getEventsPerLast10Minutes());
    }

    private AnalyticsData buildAnalyticsData() {
        return new AnalyticsData(
                getEventsPerLast10Minutes(),
                getUserEventCounts(),
                getPeakHour(),
                getTopEventTypesInLastXMinutes(10),
                getEventTypeCounts()
        );
    }

    private void updateEventTypeCounts(Event event) {
        // Count by event type
        eventTypeCounts.computeIfAbsent(event.getEventType(), k -> new AtomicInteger(0)).incrementAndGet();

    }

    private void updateUserEventCounts(Event event) {
        userEventCounts.computeIfAbsent(event.getUserId(), k -> new AtomicInteger(0)).incrementAndGet();
    }

    private void updateHourlyActivity(Event event) {
        // Count per hour (0–23)
        int hour = LocalDateTime.parse(event.getTimestamp(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).getHour();
        hourlyActivity.computeIfAbsent(hour, k -> new AtomicInteger(0)).incrementAndGet();
    }

    private void updateRecentEvents(Event event) {
        recentEvents.add(event);
    }

    private void updateMinuteCounts(Event event) {
        // Count by minute
        String minuteKey = String.format(event.getTimestamp(), MINUTE_FORMATTER);
        eventsPerMinute.computeIfAbsent(minuteKey, k -> new AtomicInteger(0)).incrementAndGet();
    }


    public Map<String, Integer> getEventTypeCounts() {
        return convertToIntegerMap(eventTypeCounts);
    }

    public Map<String, Integer> getEventsPerMinute() {
        return convertToIntegerMap(eventsPerMinute);
    }

    public Map<String, Integer> getUserEventCounts() {
        return userEventCounts.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    }

    public int getPeakHour() {
        return hourlyActivity.entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().get()))
                .map(Map.Entry::getKey)
                .orElse(-1);
    }

    public Map<String, Long> getTopEventTypesInLastXMinutes(int minutes) {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(minutes);

        return recentEvents.stream()
                .filter(e -> LocalDateTime.parse(e.getTimestamp(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).isAfter(threshold))
                .collect(Collectors.groupingBy(Event::getEventType, Collectors.counting()));
    }

    private Map<String, Integer> convertToIntegerMap(ConcurrentHashMap<String, AtomicInteger> sourceMap) {
        Map<String, Integer> result = new ConcurrentHashMap<>();
        sourceMap.forEach((k, v) -> result.put(k, v.get()));
        return result;
    }

    public Map<String, Integer> getEventsPerLast10Minutes() {
        Map<String, Integer> last10MinData = new TreeMap<>(); // TreeMap to keep order sorted
        LocalDateTime now = LocalDateTime.now();

        for (int i = 9; i >= 0; i--) { // 9,8,...0 (10 minutes)
            LocalDateTime minute = now.minusMinutes(i);
            String minuteKey = minute.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            int count = minuteCounts.getOrDefault(minuteKey, new AtomicInteger(0)).get();
            last10MinData.put(minuteKey, count);
        }

        return last10MinData;
    }



}
