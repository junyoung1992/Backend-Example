package io.spring.springbatchlecture.batch.job.listener;

import io.spring.springbatchlecture.batch.listener.CustomSkipListener;
import io.spring.springbatchlecture.batch.reader.LinkedListItemReader;
import io.spring.springbatchlecture.exception.CustomSkipException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "skipListenerJob")
public class SkipListenerJobConfig extends DefaultBatchConfiguration {

    private final CustomSkipListener customSkipListener;
    private final int chunkSize = 5;

    @Bean
    public Job job(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) throws Exception {
        return new JobBuilder("skipListenerJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step step1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("step1", jobRepository)
                .<Integer, String>chunk(chunkSize, transactionManager)
                .reader(listItemReader())
                .processor(item -> {
                    if (item == 4) {
                        throw new CustomSkipException("Process skipped.");
                    }
                    System.out.println("process : " + item);
                    return "item" + item;
                })
                .writer(items -> {
                    for (String item : items) {
                        if (item.equals("item5")) {
                            throw new CustomSkipException("Write skipped.");
                        }
                        System.out.println("write : " + item);
                    }
                })
                .faultTolerant()
                .skip(CustomSkipException.class)
                .skipLimit(3)
                .listener(customSkipListener)
                .build();
    }

    @Bean
    public ItemReader<Integer> listItemReader() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        return new LinkedListItemReader<>(list);
    }

}
