package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Scope;
import co.elastic.apm.api.Span;
import co.elastic.apm.api.Transaction;

@Service
public class RabbitMQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receive(String message) {
        Transaction transaction = ElasticApm.startTransaction();

        try (final Scope scope = transaction.activate()) {
            transaction.setName("rabbitmq-message-processing");
            transaction.setType("messaging");
            logger.info("Received message: {}", message);
            
            validateMessage(transaction, message);
            alignData(transaction, message);
            doHeavyProcessing(transaction);
            saveResult(transaction, message);
        } catch (Exception e) {
            transaction.captureException(e);
            throw e;
        } finally {
            transaction.end();
        }
    }

    private void validateMessage(Transaction transaction, String message) {
        Span span = transaction.startSpan("validation", "validate", "message");
        try (final Scope scope = span.activate()) {
            span.setName("validate-message");
            logger.info("Validating message...");
            Thread.sleep(200); // Simulate validation delay
        } catch (InterruptedException ignored) {
        } finally {
            span.end();
        }
    }

    private void alignData(Transaction transaction, String message) {
        Span span = transaction.startSpan("http", "call", "align-api");
        try (final Scope scope = span.activate()) {
            span.setName("align-data");
            logger.info("Calling external align API...");
            Thread.sleep(300); // Simulate API delay
        } catch (InterruptedException ignored) {
        } finally {
            span.end();
        }
    }

    private void doHeavyProcessing(Transaction transaction) {
        Span span = transaction.startSpan("cpu", "process", "compute");
        try (final Scope scope = span.activate()) {
            span.setName("heavy-processing");
            logger.info("Doing CPU-heavy processing...");
            long sum = 0;
            for (int i = 0; i < 1_000_000; i++) {
                sum += i;
            }
            logger.info("Processing result: {}", sum);
        } finally {
            span.end();
        }
    }

    private void saveResult(Transaction transaction, String message) {
        Span span = transaction.startSpan("storage", "save", "result");
        try (final Scope scope = span.activate()) {
            span.setName("save-result");
            logger.info("Saving result of message: {}", message);
            Thread.sleep(150); // Simulate storage delay
        } catch (InterruptedException ignored) {
        } finally {
            span.end();
        }
    }
}