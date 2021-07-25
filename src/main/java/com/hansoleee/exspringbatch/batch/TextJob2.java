package com.hansoleee.exspringbatch.batch;

import com.hansoleee.exspringbatch.custom.CustomPassThroughLineAggregator;
import com.hansoleee.exspringbatch.dto.OneDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TextJob2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chunkSize = 5;

    @Bean
    public Job textJob2Job() {
        return jobBuilderFactory.get("textJob2Job")
                .start(textJob2Step1())
                .build();
    }

    @Bean
    public Step textJob2Step1() {
        return stepBuilderFactory.get("textJob2Step1")
                .<OneDTO, OneDTO>chunk(chunkSize)
                .reader(textJob2FileReader())
                .writer(textJob2FileWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<OneDTO> textJob2FileReader() {
        FlatFileItemReader<OneDTO> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("sample/text_job2_input.txt"));
        flatFileItemReader.setLineMapper((line, lineNumber) -> new OneDTO(line));
        return flatFileItemReader;
    }

    @Bean
    public FlatFileItemWriter<OneDTO> textJob2FileWriter() {
        return new FlatFileItemWriterBuilder<OneDTO>()
                .name("textJob2FileWriter")
                .resource(new FileSystemResource("output/text_job2_output.txt"))
                .lineAggregator(new CustomPassThroughLineAggregator<>())
                .build();
    }
}
