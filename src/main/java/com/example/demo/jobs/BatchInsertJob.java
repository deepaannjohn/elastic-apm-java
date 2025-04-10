package com.example.demo.jobs;

import com.example.demo.HelloController;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Scope;
import co.elastic.apm.api.Transaction;

@Component
public class BatchInsertJob {

    private static final Logger logger = LoggerFactory.getLogger(BatchInsertJob.class);

    @Autowired
    private EmployeeRepository repository;

    @Scheduled(fixedRate = 60000)
    public void runBatchInsert() {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope scope = transaction.activate()) {
            transaction.setName("Scheduled - Batch Insert");
            transaction.setType(Transaction.TYPE_REQUEST);

            logger.info("Starting scheduled batch insert...");
            for (int i = 1; i <= 1000; i++) {
                repository.save(new Employee("Employee" + i, "Dept" + (i % 5)));
            }
            logger.warn("Batch insert complete.");
        } catch (Exception e) {
            transaction.captureException(e);
            throw e;
        } finally {
            transaction.end();
        }
    }
}
