package io.spring.springbatchlecture.batch.job.chunk;

import io.spring.springbatchlecture.batch.processor.CustomerItemProcessor;
import io.spring.springbatchlecture.entity.Customer;
import io.spring.springbatchlecture.entity.Customer2;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "jpaBatchJob")
public class JpaBatchJobConfig extends DefaultBatchConfiguration {

    private final EntityManagerFactory entityManagerFactory;
    private final int chunkSize = 5;

    @Bean
    public Job job(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("jpaBatchJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step step1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<Customer, Customer2>chunk(chunkSize, transactionManager)
                .reader(customItemReader())
                .processor(customItemProcessor())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public ItemReader<? extends Customer> customItemReader() {
        Map<String, Object> parameters = Map.of("firstname", "A%");

        return new JpaPagingItemReaderBuilder<Customer>()
                .name("jpaCursorItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("""
                        select c
                        from Customer c
                        join fetch c.address
                        where firstname like :firstname
                        order by lastname, firstname
                        """)
                .parameterValues(parameters)
                .build();
    }

    @Bean
    public ItemProcessor<? super Customer, ? extends Customer2> customItemProcessor() {
        return new CustomerItemProcessor();
    }

    @Bean
    public ItemWriter<? super Customer2> customItemWriter() {
        return new JpaItemWriterBuilder<Customer2>()
                .usePersist(true)
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

}
