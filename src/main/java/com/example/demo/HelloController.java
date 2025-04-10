package com.example.demo;

import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Observed(name = "sayHello")
    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "name", defaultValue = "World") String name) {
        logger.info("Saying hello to {}", name);
        
        String url = "https://api.agify.io/?name=" + name;
        String response = restTemplate.getForObject(url, String.class);

        logger.info("Received response from external API: {}", response);

        return "Hello, " + name + "! Prediction: " + response;
    }

    @GetMapping("/cpu-load")
    public String generateLoad(@RequestParam(defaultValue = "60") int durationSeconds) {
        logger.info("Received /cpu-load request for duration {} seconds", durationSeconds);
        getX();
        logger.info("Finished processing /cpu-load");
        return "done";
    }

    private void getX() {
        double x = Math.random();
        for (int i = 0; i < 10_000_000; i++) {
            x = Math.sin(x);
        }
    }
}
