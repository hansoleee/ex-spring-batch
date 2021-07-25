package com.hansoleee.exspringbatch.batch;

import com.hansoleee.exspringbatch.dto.OneDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TextJob1 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chunkSize = 5;

    @Bean
    public Job textJob1Job() {
        return jobBuilderFactory.get("textJob1Job")
                .start(textJob1Step1())
                .build();
    }

    @Bean
    public Step textJob1Step1() {
        return stepBuilderFactory.get("textJob1Step1")
                .<OneDTO, OneDTO>chunk(chunkSize)
                .reader(textJob1FileReader())
                .writer(oneDTO -> oneDTO.forEach(o -> log.info("{}", o)))
                .build();
    }

    @Bean
    public FlatFileItemReader<OneDTO> textJob1FileReader() {
        FlatFileItemReader<OneDTO> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("sample/text_job1_input.txt"));
        flatFileItemReader.setLineMapper((line, lineNumber) -> new OneDTO(line));
        return flatFileItemReader;
    }
}
