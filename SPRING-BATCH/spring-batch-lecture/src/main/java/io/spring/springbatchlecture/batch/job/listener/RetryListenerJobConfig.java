package io.spring.springbatchlecture.batch.job.listener;

import io.spring.springbatchlecture.batch.listener.CustomRetryListener;
import io.spring.springbatchlecture.batch.processor.CustomItemProcessor6;
import io.spring.springbatchlecture.batch.reader.LinkedListItemReader;
import io.spring.springbatchlecture.batch.writer.CustomItemWriter2;
import io.spring.springbatchlecture.exception.CustomRetryException;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "retryListenerJob")
public class RetryListenerJobConfig extends DefaultBatchConfiguration {

    private final CustomRetryListener customRetryListener;
    private final int chunkSize = 5;

    @Bean
    public Job job(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) throws Exception {
        return new JobBuilder("retryListenerJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step step1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("step1", jobRepository)
                .<Integer, String>chunk(chunkSize, transactionManager)
                .reader(listItemReader())
                .processor(customItemProcessor())
                .writer(customItemWriter())
                .faultTolerant()
                .retry(CustomRetryException.class)
                .retryLimit(2)
                .listener(customRetryListener)
                .build();
    }

    @Bean
    public ItemReader<Integer> listItemReader() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        return new LinkedListItemReader<>(list);
    }

    @Bean
    public ItemProcessor<Integer, String> customItemProcessor() {
        return new CustomItemProcessor6();
    }

    @Bean
    public ItemWriter<String> customItemWriter() {
        return new CustomItemWriter2();
    }

}
