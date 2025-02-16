package io.spring.springbatchlecture.batch.job.chunk;

import io.spring.springbatchlecture.batch.processor.CustomItemProcessor1;
import io.spring.springbatchlecture.batch.processor.CustomItemProcessor2;
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
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "compositeItemJob")
public class CompositeItemJobConfig extends DefaultBatchConfiguration {

    private final int chunkSize = 5;

    @Bean
    public Job job(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("compositeItemJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step step1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<String, String>chunk(chunkSize, transactionManager)
                .reader(new ItemReader<>() {
                    int i = 0;

                    @Override
                    public String read() throws ParseException, NonTransientResourceException {
                        return i++ >= 10 ? null : "item";
                    }
                })
                .processor(customItemProcessor())
                .writer(System.out::println)
                .build();
    }

    @Bean
    public ItemProcessor<? super String, ? extends String> customItemProcessor() {
        List<ItemProcessor<? super String, ? extends String>> itemProcessor = new ArrayList<>();
        itemProcessor.add(new CustomItemProcessor1());
        itemProcessor.add(new CustomItemProcessor2());

        return new CompositeItemProcessorBuilder<String, String>()
                .delegates(itemProcessor)
                .build();
    }

}
