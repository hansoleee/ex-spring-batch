package com.hansoleee.exspringbatch.batch;

import com.hansoleee.exspringbatch.domain.Dept;
import com.hansoleee.exspringbatch.domain.DeptTarget;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPageJob2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;

    private int chunkSize = 10;

    @Bean
    public Job jpaPageJob2BatchBuild() {
        return jobBuilderFactory.get("jpaPageJob2")
                .start(JpaPageJob2Step01())
                .build();
    }

    @Bean
    public Step JpaPageJob2Step01() {
        return stepBuilderFactory.get("jpaPageJob2Step01")
                .<Dept, DeptTarget>chunk(chunkSize)
                .reader(jpaPageJob2DbItemReader())
                .processor(jpaPageJob2Processor())
                .writer(jpaPageJob2ConsoleItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Dept> jpaPageJob2DbItemReader() {
        return new JpaPagingItemReaderBuilder<Dept>()
                .name("jpaPageJob2DbItemReader")
                .entityManagerFactory(emf)
                .pageSize(chunkSize)
                .queryString("SELECT d from Dept d order by dept_no asc")
                .build();
    }

    private ItemProcessor<Dept, DeptTarget> jpaPageJob2Processor() {
        return item -> {
            return new DeptTarget(item.getNo(), "target_" + item.getName(), item.getLoc());
        };
    }

    @Bean
    public JpaItemWriter<DeptTarget> jpaPageJob2ConsoleItemWriter() {
        JpaItemWriter<DeptTarget> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(emf);
        return jpaItemWriter;
    }
}