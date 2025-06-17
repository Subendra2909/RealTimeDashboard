package com.analytics.simulator;

import com.analytics.model.Event;
import com.github.javafaker.Faker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.*;
import java.util.concurrent.*;

public class EventSimulator {

    private static final Logger logger = LoggerFactory.getLogger(EventSimulator.class);

    private static final Faker faker = new Faker();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) throws InterruptedException {
        int totalBatches = 100; // 20 * 10 = 200 total events
        int requestsPerBatch = 50;

        ExecutorService executor = Executors.newFixedThreadPool(requestsPerBatch);

        for (int batch = 1; batch <= totalBatches; batch++) {
            List<Callable<Void>> tasks = new ArrayList<>();

            for (int i = 0; i < requestsPerBatch; i++) {
                tasks.add(() -> {
                    try {
                        Map<String, Object> event = new HashMap<>();
                        event.put("userId", faker.idNumber().valid());
                        event.put("eventType", faker.options().option("click", "scroll", "purchase", "hover"));
                        event.put("pageUrl", faker.internet().url());
                        event.put("timestamp", LocalDateTime.now().toString());

                        String json = objectMapper.writeValueAsString(event);

                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(new URI("http://localhost:8080/api/events"))
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(json))
                                .build();

                        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                        System.out.println("Sent event: " + json);
                        System.out.println("Response: " + response.statusCode());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            }

            executor.invokeAll(tasks); // send 10 requests in parallel
            System.out.println("Batch " + batch + " sent.\n");

            TimeUnit.SECONDS.sleep(1); // Wait 3 seconds before next batch
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("Simulation complete: " + (totalBatches * requestsPerBatch) + " events sent.");
    }
}
