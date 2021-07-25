package com.hansoleee.exspringbatch.batch;

import com.hansoleee.exspringbatch.dto.TwoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class CsvJob1 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chunkSize = 5;

    @Bean
    public Job csvJob1Job1() {
        return jobBuilderFactory.get("csvJob1Job1")
                .start(csvJob1Step1())
                .build();
    }

    @Bean
    public Step csvJob1Step1() {
        return stepBuilderFactory.get("csvJob1Step1")
                .<TwoDTO, TwoDTO>chunk(chunkSize)
                .reader(csvJob1FileReader())
                .writer(twoDTOs -> twoDTOs.forEach(twoDTO -> log.info("{}", twoDTO)))
                .build();
    }

    @Bean
    public FlatFileItemReader<TwoDTO> csvJob1FileReader() {
        FlatFileItemReader<TwoDTO> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("sample/csv_job1_input.csv"));
        flatFileItemReader.setLinesToSkip(1);

        DefaultLineMapper<TwoDTO> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("one", "two");
        delimitedLineTokenizer.setDelimiter(":");

        BeanWrapperFieldSetMapper<TwoDTO> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(TwoDTO.class);

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
        flatFileItemReader.setLineMapper(defaultLineMapper);

        return flatFileItemReader;
    }
}
