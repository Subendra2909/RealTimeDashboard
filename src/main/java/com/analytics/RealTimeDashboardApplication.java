package com.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RealTimeDashboardApplication {
    public static void main(String[] args){

        System.out.println("🚀 Starting Spring Boot Application...");
        SpringApplication.run(RealTimeDashboardApplication.class, args);
        System.out.println("🚀 Spring Boot Application started");
    }

}

