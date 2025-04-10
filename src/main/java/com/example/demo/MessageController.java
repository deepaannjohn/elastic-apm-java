package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final RabbitMQProducer producer;

    public MessageController(RabbitMQProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/send")
    public ResponseEntity<String> sendMessages(
            @RequestParam(defaultValue = "1") int count,
            @RequestParam String msg) {

        for (int i = 1; i <= count; i++) {
            String message = msg + " #" + i;
            producer.send(message);
            
        }

        logger.info("Finished sending messages: ");
        return ResponseEntity.ok(count + " messages sent to RabbitMQ.");
    }
}
