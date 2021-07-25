package com.hansoleee.exspringbatch.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TaskletJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job taskletJobBatchBuild() {
        return jobBuilderFactory.get("taskletJob")
                .start(taskletStep01())
                .next(taskletStep02(null))
                .build();
    }

    @Bean
    public Step taskletStep01() {
        return stepBuilderFactory.get("taskletStep01")
                .tasklet((a, b) -> {
                    log.info("-> job -> [step01]");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    @JobScope
    public Step taskletStep02(@Value("#{jobParameters[date]}") String date) {
        return stepBuilderFactory.get("taskletJobStep02")
                .tasklet((a, b) -> {
                    log.info("-> [step01] -> [step02] {}", date);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
