package com.trungnguyen.videocaptureservice.producer.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trungnguyen.videocaptureservice.model.ProducerMessage;
import com.trungnguyen.videocaptureservice.model.mongo.Metadata;
import com.trungnguyen.videocaptureservice.producer.VideoProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class VideoKafkaProducer implements VideoProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final String TOPIC_VIDEO_CAPTURE = "video-capture-topic";

    private final String DLQ_TOPIC = "dlq-topic";

    private final int MAX_RETRIES_TIME = 3;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final AtomicReference<ConcurrentLinkedQueue<ProducerMessage<Metadata>>> queue = new AtomicReference<>(new ConcurrentLinkedQueue<>());

    public VideoKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produceMessage(ProducerMessage<Metadata> message) {
        try {
            if (message.getRetry() <= MAX_RETRIES_TIME) {
                kafkaTemplate.send(TOPIC_VIDEO_CAPTURE, objectMapper.writeValueAsString(message.getData()));
            } else {
                kafkaTemplate.send(DLQ_TOPIC, objectMapper.writeValueAsString(message.getData()));
            }
        } catch (Exception e) {
            message.setRetry(message.getRetry() + 1);
            queue.get().add(message);
            retry();
        }
    }

    public void retry() {
        CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS).execute(() -> {
            ProducerMessage<Metadata> message = queue.get().poll();
            if (message != null) {
                produceMessage(message);
            }
        });
    }
}
