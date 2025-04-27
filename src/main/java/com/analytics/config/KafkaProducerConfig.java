package com.analytics.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

@Configuration
@EnableKafka
public class KafkaProducerConfig {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerConfig.class);

    public KafkaProducerConfig() {
        logger.info("Inside KafkaProducerConfig");
    }
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        logger.info("Inside producerFactory");
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        logger.info("Inside kafkaTemplate");
        return new KafkaTemplate<>(producerFactory());
    }
}
