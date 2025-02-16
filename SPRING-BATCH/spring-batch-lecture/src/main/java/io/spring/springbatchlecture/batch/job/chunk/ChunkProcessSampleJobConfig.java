package io.spring.springbatchlecture.batch.job.chunk;

import io.spring.springbatchlecture.batch.model.Customer;
import io.spring.springbatchlecture.batch.processor.CustomItemProcessor;
import io.spring.springbatchlecture.batch.reader.CustomItemReader;
import io.spring.springbatchlecture.batch.writer.CustomItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "chunkProcessSampleJob")
public class ChunkProcessSampleJobConfig extends DefaultBatchConfiguration {

    @Bean
    public Job chunkProcessSampleJob(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("chunkProcessSampleJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chunkProcessSampleJobStep1(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step chunkProcessSampleJobStep1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<Customer, Customer>chunk(3, transactionManager)
                .reader(customItemReader())
                .processor(customItemProcessor())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public CustomItemReader customItemReader() {
        return new CustomItemReader(
                List.of(new Customer("user1"),
                        new Customer("user2"),
                        new Customer("user3"),
                        new Customer("user4")));
    }

    @Bean
    public CustomItemProcessor customItemProcessor() {
        return new CustomItemProcessor();
    }

    @Bean
    public CustomItemWriter customItemWriter() {
        return new CustomItemWriter();
    }

}
