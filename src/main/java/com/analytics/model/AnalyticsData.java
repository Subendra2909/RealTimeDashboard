package com.analytics.model;

import java.util.Map;

public class AnalyticsData {
    private Map<String, Integer> eventsPerMinute;
    private Map<String, Integer> eventsPerUser;
    private int peakHour;
    private Map<String, Long> topEventTypes;
    private Map<String, Integer> eventTypeCounts;

    // Constructors
    public AnalyticsData() {}

    public AnalyticsData(Map<String, Integer> eventsPerMinute,
                         Map<String, Integer> eventsPerUser,
                         int PeakHour,
                         Map<String, Long> topEventTypes,
                         Map<String, Integer> eventTypeCounts) {
        this.eventsPerMinute = eventsPerMinute;
        this.eventsPerUser = eventsPerUser;
        this.topEventTypes = topEventTypes;
        this.peakHour = PeakHour;
        this.eventTypeCounts = eventTypeCounts;
    }

    // Getters and Setters
    public Map<String, Integer> getEventsPerMinute() {
        return eventsPerMinute;
    }

    public void setEventsPerMinute(Map<String, Integer> eventsPerMinute) {
        this.eventsPerMinute = eventsPerMinute;
    }

    public Map<String, Integer> getEventsPerUser() {
        return eventsPerUser;
    }

    public void setEventsPerUser(Map<String, Integer> eventsPerUser) {
        this.eventsPerUser = eventsPerUser;
    }

    public int getPeakHour() {
        return peakHour;
    }

    public void setPeakHour(int peakHour) {
        this.peakHour = peakHour;
    }

    public Map<String, Long> getTopEventTypes() {
        return topEventTypes;
    }

    public void setTopEventTypes(Map<String, Long> topEventTypes) {
        this.topEventTypes = topEventTypes;
    }

    public Map<String, Integer> getEventTypeCounts() {
        return eventTypeCounts;
    }

    public void setEventTypeCounts(Map<String, Integer> eventTypeCounts) {
        this.eventTypeCounts = eventTypeCounts;
    }
}
