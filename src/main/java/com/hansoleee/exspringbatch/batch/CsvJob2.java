package com.hansoleee.exspringbatch.batch;

import com.hansoleee.exspringbatch.custom.CustomBeanWrapperFieldExtractor;
import com.hansoleee.exspringbatch.dto.TwoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class CsvJob2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chunkSize = 5;

    @Bean
    public Job csvJob2Job() {
        return jobBuilderFactory.get("csvJob2Job2")
                .start(csvJob2Step1())
                .build();
    }

    @Bean
    public Step csvJob2Step1() {
        return stepBuilderFactory.get("csvJob2Step1")
                .<TwoDTO, TwoDTO>chunk(chunkSize)
                .reader(csvJob2FileReader())
                .processor(consoleLog())
                .processor(changeData())
                .writer(csvJob2FileWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<TwoDTO> csvJob2FileReader() {
        FlatFileItemReader<TwoDTO> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("sample/csv_job2_input.csv"));
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

    private ItemProcessor<? super TwoDTO, ? extends TwoDTO> consoleLog() {
        return twoDTO -> {
            log.info("{}", twoDTO);
            return null;
        };
    }

    private ItemProcessor<? super TwoDTO, ? extends TwoDTO> changeData() {
        return twoDTO -> {
            return new TwoDTO(twoDTO.getTwo(), twoDTO.getOne());
        };
    }

    @Bean
    public FlatFileItemWriter<TwoDTO> csvJob2FileWriter() {
        CustomBeanWrapperFieldExtractor<TwoDTO> customBeanWrapperFieldExtractor = new CustomBeanWrapperFieldExtractor<>();
        customBeanWrapperFieldExtractor.setNames(new String[]{"one", "two"});
        customBeanWrapperFieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<TwoDTO> delimitedLineAggregator = new DelimitedLineAggregator<>();
        delimitedLineAggregator.setDelimiter(",");
        delimitedLineAggregator.setFieldExtractor(customBeanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<TwoDTO>()
                .name("csvJob2FileWriter")
                .resource(new FileSystemResource("output/csv_job2_output.csv"))
                .lineAggregator(delimitedLineAggregator)
                .build();
    }
}
