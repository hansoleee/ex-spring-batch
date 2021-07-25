package com.hansoleee.exspringbatch.batch;

import com.hansoleee.exspringbatch.domain.Dept;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPageJob1 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;

    private int chunkSize = 10;

    @Bean
    public Job jpaPageJob1Job() {
        return jobBuilderFactory.get("jpaPageJob1Job")
                .start(JpaPageJob1Step01())
                .build();
    }

    @Bean
    public Step JpaPageJob1Step01() {
        return stepBuilderFactory.get("JpaPageJob1Step01")
                .<Dept, Dept>chunk(chunkSize)
                .reader(jpaPageJob1DbItemReader())
                .writer(jpaPageJob1ConsoleItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Dept> jpaPageJob1DbItemReader() {
        return new JpaPagingItemReaderBuilder<Dept>()
                .name("jpaPageJob1DbItemReader")
                .entityManagerFactory(emf)
                .pageSize(chunkSize)
                .queryString("SELECT d from Dept d order by dept_no asc")
                .build();
    }

    @Bean
    public ItemWriter<Dept> jpaPageJob1ConsoleItemWriter() {
        return items -> items.forEach(dept -> log.info("{}", dept));
    }
}