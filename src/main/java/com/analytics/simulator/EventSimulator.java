package com.analytics.simulator;

import com.github.javafaker.Faker;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EventSimulator {
    private static final Faker faker = new Faker();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) throws Exception {
        for(int i=0;i<100;i++){
            Map<String, Object> event = new HashMap<>();
            event.put("userId", faker.idNumber().valid());
            event.put("eventType", faker.options().option("click", "scroll", "purchase", "hover"));
            event.put("pageUrl", faker.internet().url());
            event.put("timestamp", LocalDateTime.now().toString());

            String json = objectMapper.writeValueAsString(event);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/events"))  // Make sure this matches your Spring endpoint
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Sent event: " + json);
            System.out.println("Response: " + response.statusCode());

            TimeUnit.SECONDS.sleep(2);  // Send every 2 seconds
        }
        System.out.println("Simulation complete: 100 events sent.");
    }
}
