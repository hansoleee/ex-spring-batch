package com.hansoleee.exspringbatch.batch;

import com.hansoleee.exspringbatch.dto.CoinMarketDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.beans.JavaBean;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JsonJob1 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int CHUNK_SIZE = 10;

    @Bean
    public Job jsonJob1Job() {
        return jobBuilderFactory.get("jsonJob1Job")
                .start(jsonJob1Step1())
                .build();
    }

    @Bean
    public Step jsonJob1Step1() {
        return stepBuilderFactory.get("jsonJob1Step1")
                .<CoinMarketDTO, CoinMarketDTO>chunk(CHUNK_SIZE)
                .reader(jsonJob1Reader1())
                .writer(coinMarkets -> {
                    coinMarkets.forEach(coinMarket -> log.info("{}", coinMarket));
                })
                .build();
    }

    @Bean
    public JsonItemReader<CoinMarketDTO> jsonJob1Reader1() {
        return new JsonItemReaderBuilder<CoinMarketDTO>()
                .name("jsonJob1Reader1")
                .jsonObjectReader(new JacksonJsonObjectReader<>(CoinMarketDTO.class))
                .resource(new ClassPathResource("sample/json_job1.input.json"))
                .build();
    }
}
