package com.hansoleee.exspringbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class ExSpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExSpringBatchApplication.class, args);
    }

}
